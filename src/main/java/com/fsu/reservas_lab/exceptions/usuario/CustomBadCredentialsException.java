package com.fsu.reservas_lab.exceptions.usuario;

import org.springframework.security.authentication.BadCredentialsException;

public class CustomBadCredentialsException extends BadCredentialsException {
    public CustomBadCredentialsException() {
        super("Email ou senha inválidos! Por favor, tente novamente.");
    }
}
