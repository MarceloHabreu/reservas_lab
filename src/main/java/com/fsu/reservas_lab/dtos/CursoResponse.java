package com.fsu.reservas_lab.dtos;

import com.fsu.reservas_lab.entities.Curso;

public record CursoResponse(Long id, String nome, String codigo) {
    public static CursoResponse fromEntity(Curso c) {
        return new CursoResponse(c.getId(), c.getNome(), c.getCodigo());
    }
}
