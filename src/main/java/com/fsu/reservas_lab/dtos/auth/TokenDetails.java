package com.fsu.reservas_lab.dtos.auth;

public record TokenDetails(String token, Long expiresIn) {
}