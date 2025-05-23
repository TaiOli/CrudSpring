package med.voll.api.domain.paciente;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Page<Paciente> findAllByAtivoTrue(Pageable paginacao);

    @Modifying
    @Transactional
    @Query("UPDATE Paciente p SET p.ativo = false WHERE p.id = :id")
    void excluirLogicamente(@Param("id") Long id);

    @Query("SELECT p.ativo FROM Paciente p WHERE p.id = :id")
    Boolean findAtivoById(@Param("id") Long id);
}