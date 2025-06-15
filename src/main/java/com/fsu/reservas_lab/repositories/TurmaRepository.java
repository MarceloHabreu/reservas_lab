package com.fsu.reservas_lab.repositories;

import com.fsu.reservas_lab.entities.Turma;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TurmaRepository extends JpaRepository<Turma, Long> {
    Optional<Object> findByCodigo(String codigo);
}
