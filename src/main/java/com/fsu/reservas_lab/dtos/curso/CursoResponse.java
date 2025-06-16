package com.fsu.reservas_lab.dtos.curso;

import com.fsu.reservas_lab.dtos.usuario.UsuarioResponse;
import com.fsu.reservas_lab.entities.Curso;

public record CursoResponse(Long id, String nome, String codigo, UsuarioResponse coordenadorCurso) {
    public static CursoResponse fromEntity(Curso c) {
        return new CursoResponse(c.getId(), c.getNome(), c.getCodigo(), UsuarioResponse.fromEntity(c.getCoordenador()));
    }
}
