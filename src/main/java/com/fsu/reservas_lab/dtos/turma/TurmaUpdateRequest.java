package com.fsu.reservas_lab.dtos.turma;

public record TurmaUpdateRequest(String codigo, String disciplina, Integer numeroAlunos, String periodoLetivo, Long cursoId, Long professorId) {
}
