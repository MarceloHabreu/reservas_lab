package com.fsu.reservas_lab.exceptions.reserva;

public class OrderApprovalInvalidatedException extends RuntimeException {
    public OrderApprovalInvalidatedException(String message) {
        super(message);
    }
}
