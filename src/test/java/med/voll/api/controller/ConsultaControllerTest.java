package med.voll.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import med.voll.api.domain.consulta.AgendaDeConsultas;
import med.voll.api.domain.consulta.Consulta;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.consulta.DadosDetalhamentoConsulta;
import med.voll.api.domain.medico.Especialidade;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.paciente.Paciente;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class ConsultaControllerTest {

        @Autowired
        private MockMvc mvc;

        @Autowired
        private JacksonTester<DadosAgendamentoConsulta> dadosAgendamentoConsultaJson;

        @Autowired
        private JacksonTester<DadosDetalhamentoConsulta> dadosDetalhamentoConsultaJson;

        @MockitoBean
        private AgendaDeConsultas agendaDeConsultas;

        @Test
        @DisplayName("Deveria devolver código http 400 quando informações estão inválidas")
        @WithMockUser
        void agendar_cenario1() throws Exception {
                MvcResult result = mvc.perform(post("/consultas"))
                                .andReturn();

                int status = result.getResponse().getStatus();

                assertThat(status).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("Deveria devolver código http 200 quando informações estão válidas")
        @WithMockUser
        void agendar_cenario2() throws Exception {
                var data = LocalDateTime.now().plusHours(1);
                var especialidade = Especialidade.CARDIOLOGIA;

                var medico = new Medico(5L, "Dr. João", "joao@email.com", "11999999999", "CRM123",
                                Especialidade.CARDIOLOGIA, null, true);

                var paciente = new Paciente(1L, "Carlos Silva", "carlos@email.com", "11999999999",
                                "12345678900", null, true);

                var consulta = new Consulta(1L, medico, paciente, data);

                when(agendaDeConsultas.agendar(any())).thenReturn(consulta);

                var response = mvc
                                .perform(
                                                post("/consultas")
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .content(dadosAgendamentoConsultaJson.write(
                                                                                new DadosAgendamentoConsulta(2L, 5L,
                                                                                                data, especialidade))
                                                                                .getJson()))
                                .andReturn()
                                .getResponse();

                assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

                var jsonEsperado = dadosDetalhamentoConsultaJson.write(
                                new DadosDetalhamentoConsulta(consulta)).getJson();

                assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
        }

}