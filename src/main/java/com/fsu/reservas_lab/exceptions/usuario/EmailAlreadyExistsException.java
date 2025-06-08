package com.fsu.reservas_lab.exceptions.usuario;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException() {
        super("O email que você está tentando cadastrar já se encontra em uso! Por favor tente outro.");
    }
}
