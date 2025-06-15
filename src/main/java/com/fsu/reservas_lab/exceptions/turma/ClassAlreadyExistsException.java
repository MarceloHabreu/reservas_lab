package com.fsu.reservas_lab.exceptions.turma;

public class ClassAlreadyExistsException extends RuntimeException {
    public ClassAlreadyExistsException() {
        super("Ja existe uma turma com este código! Verifique turmas cadastradas." );
    }
}
