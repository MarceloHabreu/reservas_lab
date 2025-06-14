package com.fsu.reservas_lab.dtos.laboratorio;

import com.fsu.reservas_lab.dtos.usuario.UsuarioShortResponse;
import com.fsu.reservas_lab.entities.Laboratorio;
import com.fsu.reservas_lab.entities.enums.StatusReserva;

import java.util.List;

public record LaboratorioResponse(Long id, String nome, Integer capacidade, String localizacao,
                                  UsuarioShortResponse coordenadorLab,
                                  List<UsuarioShortResponse> tecnicos, boolean possuiReservasAtivas) {

    public static LaboratorioResponse fromEntity(Laboratorio laboratorio) {
        boolean possuiReservasAtivas = laboratorio.getReservas().stream()
                .anyMatch(reserva -> reserva.getStatusReserva() == StatusReserva.ATIVA);

        return new LaboratorioResponse(
                laboratorio.getId(),
                laboratorio.getNome(),
                laboratorio.getCapacidade(),
                laboratorio.getLocalizacao(),
                UsuarioShortResponse.fromEntity(laboratorio.getCoordenador()),
                laboratorio.getTecnicos().stream().map(UsuarioShortResponse::fromEntity).toList(),
                possuiReservasAtivas
        );
    }
}


