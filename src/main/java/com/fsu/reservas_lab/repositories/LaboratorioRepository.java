package com.fsu.reservas_lab.repositories;

import com.fsu.reservas_lab.entities.Laboratorio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LaboratorioRepository extends JpaRepository<Laboratorio, Long> {
}
