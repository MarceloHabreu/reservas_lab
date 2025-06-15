package com.fsu.reservas_lab.dtos;

import java.time.LocalDate;

public record TurmaCreateRequest(String codigo, String disciplina, Integer numeroAlunos, String periodoLetivo, Long cursoId, Long professorId) {
}
