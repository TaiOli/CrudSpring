package med.voll.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
public class ConsultaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("Deveria devolver código http 400 quando informações estao inválidas")
    @WithMockUser
    void agendar_cenario1() throws Exception {
        MvcResult result = mvc.perform(post("/consultas"))
                .andReturn();

        int status = result.getResponse().getStatus();

        assertThat(status).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}