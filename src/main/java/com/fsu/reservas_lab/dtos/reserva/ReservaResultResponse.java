package com.fsu.reservas_lab.dtos.reserva;

public record ReservaResultResponse(
        String message,
        ReservaResponse reserva
) {}
