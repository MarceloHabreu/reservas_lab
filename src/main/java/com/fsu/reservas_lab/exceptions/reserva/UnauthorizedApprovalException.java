package com.fsu.reservas_lab.exceptions.reserva;

public class UnauthorizedApprovalException extends RuntimeException {
    public UnauthorizedApprovalException(String message) {
        super(message);
    }
}
