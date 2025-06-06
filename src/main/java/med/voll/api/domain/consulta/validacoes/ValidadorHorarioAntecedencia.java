package med.voll.api.domain.consulta.validacoes;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import java.time.Duration;
import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;

@Component
public class ValidadorHorarioAntecedencia implements ValidadorAgendamentoDeConsulta {

    public void validar(DadosAgendamentoConsulta dados) {
        var dataConsulta = dados.data();
        var agora = LocalDateTime.now();
        var diferencaEmMinutos = Duration.between(agora, dataConsulta).toMinutes();

        System.out.println("Agora: " + agora);
        System.out.println("Data Consulta: " + dataConsulta);
        System.out.println("Diferença em minutos: " + diferencaEmMinutos);

        if (diferencaEmMinutos < 30) {
            throw new ValidacaoException("Consulta deve ser agendada com antecedência mínima de 30 minutos");
        }
    }
}