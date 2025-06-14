package com.fsu.reservas_lab.dtos.usuario;

import com.fsu.reservas_lab.entities.Usuario;
import com.fsu.reservas_lab.entities.enums.TipoUsuario;

public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        TipoUsuario tipo,
        String matricula,
        Long cursoId,
        String cursoNome
) {
    public static UsuarioResponse fromEntity(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTipo(),
                usuario.getMatricula(),
                usuario.getCurso() != null ? usuario.getCurso().getId() : null,
                usuario.getCurso() != null ? usuario.getCurso().getNome() : null
        );
    }
}