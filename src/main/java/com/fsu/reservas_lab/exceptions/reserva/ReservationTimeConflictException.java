package com.fsu.reservas_lab.exceptions.reserva;

public class ReservationTimeConflictException extends RuntimeException {
    public ReservationTimeConflictException() {
        super("Já existe uma reserva para esse laboratório e horário com outra turma.");
    }
}
