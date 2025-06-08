package com.fsu.reservas_lab.dtos.auth;

public record AuthResponse(String message,
                           String token,
                           Long expiresIn) {
}
