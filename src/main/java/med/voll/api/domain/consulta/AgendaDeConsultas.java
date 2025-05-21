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
        // Verificar se id do paciente é informado
        var paciente = pacienteRepository.findById(dados.idPaciente())
                .orElseThrow(() -> new ValidacaoException("Id do paciente informado não existe!"));

        // Verificar se id do médico é informado
        var medico = medicoRepository.findById(dados.idMedico())
                .orElseThrow(() -> new ValidacaoException("ID do médico não encontrado"));

        // Validar o horário de funcionamento da clínica
        validadorHorario.validar(dados);

        if (medico == null) {
            medico = escolherMedico(dados);
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