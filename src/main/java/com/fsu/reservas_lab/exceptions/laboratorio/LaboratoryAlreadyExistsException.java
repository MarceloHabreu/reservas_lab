package com.fsu.reservas_lab.exceptions.laboratorio;

public class LaboratoryAlreadyExistsException extends RuntimeException {
    public LaboratoryAlreadyExistsException() {
        super("Este laboratorio já existe! Verifique laboratorios cadastrados.");
    }
}
