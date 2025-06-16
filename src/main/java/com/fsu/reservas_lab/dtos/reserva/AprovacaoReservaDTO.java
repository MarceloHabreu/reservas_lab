package com.fsu.reservas_lab.dtos.reserva;

import com.fsu.reservas_lab.entities.enums.TipoAprovacao;
import jakarta.validation.constraints.NotNull;

public record AprovacaoReservaDTO(
        @NotNull Boolean aprovado,
        String obs
) {}