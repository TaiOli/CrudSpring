package med.voll.api.domain.consulta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.validacoes.ValidadorHorarioFuncionamentoClinica;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;

@Service
public class AgendaDeConsultas {

    @Autowired
    private ValidadorHorarioFuncionamentoClinica validadorHorario;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    public Consulta agendar(DadosAgendamentoConsulta dados) {
        // Verificar se paciente existe e se está ativo
        var paciente = pacienteRepository.findById(dados.idPaciente())
                .orElseThrow(() -> new ValidacaoException("Id do paciente informado não existe!"));
        if (!paciente.getAtivo()) {
            throw new ValidacaoException("Paciente está inativo no sistema!");
        }

        // Verificar se id do médico é informado, se existe e se está ativo
        Medico medico = null;
        if (dados.idMedico() != null) {
            medico = medicoRepository.findById(dados.idMedico())
                    .orElseThrow(() -> new ValidacaoException("Id do médico informado não existe!"));
            if (!medico.getAtivo()) {
                throw new ValidacaoException("Médico está inativo no sistema!");
            }
        }

        // Validar a antecedência mínima de 30 minutos
        var agora = java.time.LocalDateTime.now();
        if (dados.data().isBefore(agora.plusMinutes(30))) {
            throw new ValidacaoException("Consulta deve ser agendada com antecedência mínima de 30 minutos!");
        }

        // Validar o horário de funcionamento da clínica
        validadorHorario.validar(dados);

        // Verificar se o paciente já tem consulta no mesmo dia
        var inicioDia = dados.data().toLocalDate().atStartOfDay();
        var fimDia = inicioDia.plusDays(1).minusNanos(1);
        boolean pacienteTemConsultaNoDia = consultaRepository.existsByPacienteIdAndDataBetween(
                dados.idPaciente(), inicioDia, fimDia);
        if (pacienteTemConsultaNoDia) {
            throw new ValidacaoException("Paciente já possui consulta agendada para este dia!");
        }

        if (medico == null) {
            medico = escolherMedico(dados);
        }

        // Escolher o médico se não for informado
        if (medico == null) {
            if (dados.especialidade() == null) {
                throw new ValidacaoException("Especialidade é obrigatória quando médico não for escolhido!");
            }
            medico = medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(), dados.data());
            if (medico == null) {
                throw new ValidacaoException("Não existe médico disponível para esta especialidade e data!");
            }
        }

        // Verificar se o médico já tem consulta agendada na mesma data/hora
        boolean medicoTemConsultaNaData = consultaRepository.existsByMedicoIdAndData(medico.getId(), dados.data());
        if (medicoTemConsultaNaData) {
            throw new ValidacaoException("Médico já possui consulta agendada neste horário!");
        }

        // Criar consulta e salvar
        var consulta = new Consulta(null, medico, paciente, dados.data());
        consultaRepository.save(consulta);

        return consulta;
    }

    private Medico escolherMedico(DadosAgendamentoConsulta dados) {
        if (dados.idMedico() != null) {
            return medicoRepository.getReferenceById(dados.idMedico());
        }

        if (dados.especialidade() == null) {
            throw new ValidacaoException("Especialidade é obrigatória quando médico não for escolhido!");
        }

        var medico = medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(), dados.data());
        if (medico == null) {
            throw new ValidacaoException("Não existe médico disponível nessa data para a especialidade informada.");
        }

        return medico;
    }

}
