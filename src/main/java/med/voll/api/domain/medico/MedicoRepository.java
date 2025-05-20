package med.voll.api.domain.medico;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import jakarta.transaction.Transactional;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

    Page<Medico> findAllByAtivoTrue(Pageable paginacao);

    @Modifying
    @Transactional
    @Query("DELETE FROM Medico m WHERE m.id = :id")
    void excluirFisicamente(Long id);

    @Query(value = """
                select * from medico m
                where m.ativo = 1
                and m.especialidade = :especialidade
                and m.id not in (
                    select c.medico_id from consulta c
                    where c.data = :data
                )
                order by rand()
                limit 1
            """, nativeQuery = true)
    Medico escolherMedicoAleatorioLivreNaData(Especialidade especialidade, LocalDateTime data);

}