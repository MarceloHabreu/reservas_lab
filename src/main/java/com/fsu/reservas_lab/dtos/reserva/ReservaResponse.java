package com.fsu.reservas_lab.dtos.reserva;

import com.fsu.reservas_lab.dtos.laboratorio.LaboratorioResponse;
import com.fsu.reservas_lab.dtos.turma.TurmaResponse;
import com.fsu.reservas_lab.dtos.usuario.UsuarioResponse;
import com.fsu.reservas_lab.entities.Reserva;
import com.fsu.reservas_lab.entities.enums.StatusPedidoReserva;
import com.fsu.reservas_lab.entities.enums.StatusReserva;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservaResponse(
        Long id,
        String atividade,
        LocalDate data,
        LocalTime horaInicio,
        LocalTime horaFim,
        String observacoes,
        StatusReserva statusReserva,
        StatusPedidoReserva statusPedidoReserva,
        LaboratorioResponse laboratorio,
        TurmaResponse turma,
        UsuarioResponse solicitante
) {
    public static ReservaResponse fromEntity(Reserva reserva) {
        return new ReservaResponse(
                reserva.getId(),
                reserva.getAtividade(),
                reserva.getData(),
                reserva.getHoraInicio(),
                reserva.getHoraFim(),
                reserva.getObservacoes(),
                reserva.getStatusReserva(),
                reserva.getStatusPedidoReserva(),
                LaboratorioResponse.fromEntity(reserva.getLaboratorio()),
                TurmaResponse.fromEntity(reserva.getTurma()),
                UsuarioResponse.fromEntity(reserva.getSolicitante())
        );
    }
}
