package com.fsu.reservas_lab.exceptions.curso;

public class CourseAlreadyExistsException extends RuntimeException {
    public CourseAlreadyExistsException() {
        super("Este curso já existe! Verifique cursos cadastrados." );
    }
}
