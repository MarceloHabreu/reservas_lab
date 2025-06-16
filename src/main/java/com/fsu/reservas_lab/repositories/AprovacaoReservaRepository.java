package com.fsu.reservas_lab.repositories;

import com.fsu.reservas_lab.entities.AprovacaoReserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AprovacaoReservaRepository extends JpaRepository<AprovacaoReserva, Long> {
}
