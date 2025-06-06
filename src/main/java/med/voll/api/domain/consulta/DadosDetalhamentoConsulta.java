package med.voll.api.domain.consulta;

import java.time.LocalDateTime;

public record DadosDetalhamentoConsulta(Long id, Long idMedico, String nomeMedico, Long idPaciente,
                LocalDateTime data) {

        public DadosDetalhamentoConsulta(Consulta consulta) {
                this(
                                consulta.getId(),
                                consulta.getMedico().getId(),
                                consulta.getMedico().getNome(),
                                consulta.getPaciente().getId(),
                                consulta.getData());
        }

}
