package com.fsu.reservas_lab.repositories;

import com.fsu.reservas_lab.entities.Laboratorio;
import com.fsu.reservas_lab.entities.enums.StatusLaboratorio;
import com.fsu.reservas_lab.entities.enums.StatusReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LaboratorioRepository extends JpaRepository<Laboratorio, Long> {
    Optional<Laboratorio> findByNome(String nome);

    @Query("SELECT COUNT(r) > 0 FROM Reserva r WHERE r.laboratorio.id = :laboratorioId AND r.statusReserva = 'ATIVA'")
    boolean hasActiveReservations(@Param("laboratorioId") Long laboratorioId);

    @Query("SELECT l FROM Laboratorio l WHERE :status IS NULL OR l.statusLaboratorio = :status")
    List<Laboratorio> findByStatus(@Param("status") StatusLaboratorio status);


}

