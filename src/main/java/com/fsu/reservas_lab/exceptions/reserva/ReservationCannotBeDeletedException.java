package com.fsu.reservas_lab.exceptions.reserva;

public class ReservationCannotBeDeletedException extends RuntimeException {
    public ReservationCannotBeDeletedException() {
        super("A reserva não pode ser deletada pois já não esta em estado PENDENTE");
    }
}
