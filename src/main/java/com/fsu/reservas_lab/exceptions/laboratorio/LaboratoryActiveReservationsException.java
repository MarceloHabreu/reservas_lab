package com.fsu.reservas_lab.exceptions.laboratorio;

public class LaboratoryActiveReservationsException extends RuntimeException {
    public LaboratoryActiveReservationsException() {
        super("Não é possível excluir este laboratório, pois há reservas ativas no momento.");
    }
}
