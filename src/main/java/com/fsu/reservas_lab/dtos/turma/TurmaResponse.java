package com.fsu.reservas_lab.dtos.turma;

import com.fsu.reservas_lab.dtos.curso.CursoResponse;
import com.fsu.reservas_lab.dtos.usuario.UsuarioResponse;
import com.fsu.reservas_lab.entities.Turma;

public record TurmaResponse(Long id, String codigo, String disciplina,Integer numeroAlunos, String periodoLetivo, CursoResponse curso, UsuarioResponse professor) {
    public static TurmaResponse fromEntity(Turma t){
        return new TurmaResponse(t.getId(), t.getCodigo(), t.getDisciplina(),t.getNumeroAlunos(), t.getPeriodoLetivo(), CursoResponse.fromEntity(t.getCurso()), UsuarioResponse.fromEntity(t.getProfessor()));
    }
}
