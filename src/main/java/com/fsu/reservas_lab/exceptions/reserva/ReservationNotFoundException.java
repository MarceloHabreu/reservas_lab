package com.fsu.reservas_lab.exceptions.reserva;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException() {
        super("Reserva não encontrada!");
    }
}
