package com.fsu.reservas_lab.dtos.reserva;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservaUpdateRequest(
        Long laboratorioId,
        Long turmaId,
        String atividade,
        LocalDate data,
        LocalTime horaInicio,
        LocalTime horaFim,
        String observacoes
) {}
