package com.fsu.reservas_lab.exceptions.laboratorio;

public class LaboratoryNotFoundException extends RuntimeException {
  public LaboratoryNotFoundException() {
    super("Laboratorio não encontrado! Por favor tente novamente.");
  }
}
