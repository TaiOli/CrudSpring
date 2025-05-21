package med.voll.api.domain.consulta.validacoes;

import java.time.DayOfWeek;
import java.time.LocalTime;

import org.springframework.stereotype.Component;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;

@Component
public class ValidadorHorarioFuncionamentoClinica {

    public void validar(DadosAgendamentoConsulta dados) {
        var dataConsulta = dados.data();
        var horario = dataConsulta.toLocalTime();

        System.out.println("Validando horário: " + horario);

        var domingo = dataConsulta.getDayOfWeek().equals(DayOfWeek.SUNDAY);
        var abertura = LocalTime.of(7, 0);
        var fechamento = LocalTime.of(18, 0);

        var antesDaAberturaDaClinica = horario.isBefore(abertura);
        var depoisDoEncerramentoDaClinica = horario.isAfter(fechamento);

        if (domingo || antesDaAberturaDaClinica || depoisDoEncerramentoDaClinica) {
            throw new ValidacaoException("Consulta fora do horário de funcionamento da clínica");
        }
    }

}
