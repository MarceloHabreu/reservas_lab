package com.fsu.reservas_lab.exceptions.usuario;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("Usuário não encontrado! Por favor tente novamente.");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}

