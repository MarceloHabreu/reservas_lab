package com.fsu.reservas_lab.exceptions.usuario;

public class EnrollmentAlreadyExistsException extends RuntimeException {
    public EnrollmentAlreadyExistsException() {
        super("Verifique se sua matricula está correta e tente novamente!");
    }
}
