package com.fsu.reservas_lab.exceptions.curso;

public class CourseNotFoundException extends RuntimeException {
  public CourseNotFoundException() {
    super("Curso não encontrado! Por favor tente novamente.");
  }
}
