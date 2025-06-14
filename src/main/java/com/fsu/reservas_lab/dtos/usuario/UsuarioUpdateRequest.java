package com.fsu.reservas_lab.dtos.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateRequest(
        @NotBlank @Size(max = 100) String nome,
        @NotBlank @Email
        @Size(max = 255) String email,
        String senha,
        String matricula,
        Long cursoId
) {}
