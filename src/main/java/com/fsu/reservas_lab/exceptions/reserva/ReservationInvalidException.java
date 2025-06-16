package com.fsu.reservas_lab.exceptions.reserva;

public class ReservationInvalidException extends RuntimeException {
    public ReservationInvalidException(String message) {
        super(message);
    }
}
