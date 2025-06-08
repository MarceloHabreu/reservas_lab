package com.fsu.reservas_lab.dtos.auth;

public record LoginRequest(
        String email,
        String senha) {
}
