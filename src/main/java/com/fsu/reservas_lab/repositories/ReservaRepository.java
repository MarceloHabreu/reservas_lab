package com.fsu.reservas_lab.repositories;

import com.fsu.reservas_lab.entities.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
}
