package com.fsu.reservas_lab.exceptions.usuario;

public class NameAlreadyExistsException extends RuntimeException {
    public NameAlreadyExistsException() {
        super("O nome que você está tentando cadastrar já se encontra em uso! Por favor tente outro.");
    }
}
