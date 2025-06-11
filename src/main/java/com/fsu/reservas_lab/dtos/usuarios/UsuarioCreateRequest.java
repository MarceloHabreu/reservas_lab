package com.fsu.reservas_lab.dtos.usuarios;

import com.fsu.reservas_lab.entities.enums.TipoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioCreateRequest(
        @NotBlank @Size(max = 100) String nome,
        @NotBlank @Email @Size(max = 255) String email,
        @NotBlank @Size(min = 8) String senha,
        @NotNull TipoUsuario tipo,
        String matricula,
        Long cursoId
) {}