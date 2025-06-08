package com.fsu.reservas_lab.dtos.auth;

import com.fsu.reservas_lab.entities.enums.TipoCurso;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        String nome,
        String email,
        String senha,
        String matricula,
        TipoCurso curso
) {
}
