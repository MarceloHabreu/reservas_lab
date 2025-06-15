package com.fsu.reservas_lab.dtos.laboratorio;

import com.fsu.reservas_lab.dtos.usuario.UsuarioShortResponse;
import com.fsu.reservas_lab.entities.Laboratorio;
import com.fsu.reservas_lab.entities.enums.StatusLaboratorio;
import com.fsu.reservas_lab.entities.enums.StatusReserva;

import java.util.List;

public record LaboratorioResponse(Long id, String nome, Integer capacidade, String localizacao,
                                  UsuarioShortResponse coordenadorLab,
                                  List<UsuarioShortResponse> tecnicos, StatusLaboratorio status) {

    public static LaboratorioResponse fromEntity(Laboratorio laboratorio) {
        return new LaboratorioResponse(
                laboratorio.getId(),
                laboratorio.getNome(),
                laboratorio.getCapacidade(),
                laboratorio.getLocalizacao(),
                UsuarioShortResponse.fromEntity(laboratorio.getCoordenador()),
                laboratorio.getTecnicos().stream().map(UsuarioShortResponse::fromEntity).toList(),
                laboratorio.getStatusLaboratorio()
        );
    }
}


