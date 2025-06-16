package com.fsu.reservas_lab.exceptions.reserva;

public class ReservationCannotBeUpdatedException extends RuntimeException {
    public ReservationCannotBeUpdatedException() {
        super("A reserva não pode ser atualizada pois ja está ATIVA.");
    }

    public ReservationCannotBeUpdatedException(String message) {
        super(message);
    }
}
