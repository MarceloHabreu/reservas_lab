package com.fsu.reservas_lab.dtos.laboratorio;

import java.util.List;

public record LaboratorioCreateRequest(String nome, Integer capacidade, String localizacao, Long coordenadorLab, List<Long> tecnicos) {
}
