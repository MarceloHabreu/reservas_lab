package com.fsu.reservas_lab.exceptions.turma;

public class ClassNotFoundException extends RuntimeException {
  public ClassNotFoundException() {
    super("Turma não encontrada! Por favor tente novamente.");
  }
}
