package com.fsu.reservas_lab.repositories;

import com.fsu.reservas_lab.entities.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
}
