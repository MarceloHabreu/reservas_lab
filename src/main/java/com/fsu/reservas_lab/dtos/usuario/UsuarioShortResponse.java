package com.fsu.reservas_lab.dtos.usuario;

import com.fsu.reservas_lab.entities.Usuario;

public record UsuarioShortResponse(Long id, String nome, String email, String matricula) {
    public static UsuarioShortResponse fromEntity(Usuario u){
        return new UsuarioShortResponse(u.getId(),u.getNome(),u.getEmail(), u.getMatricula());
    }
}
