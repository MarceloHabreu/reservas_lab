package com.fsu.reservas_lab.exceptions.turma;

public class TeacherNotAllowedException extends RuntimeException {
    public TeacherNotAllowedException(String message) {
        super(message);
    }
}
