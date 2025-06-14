package com.fsu.reservas_lab.entities.enums;

public enum StatusPedidoReserva {
    PENDENTE("pendente"),
    APROVADO_LABORATORIO("aprovado_laboratorio"),
    APROVADO_CURSO("aprovado_curso"),
    APROVADO("aprovado"),
    REJEITADA("rejeitada");

    private final String dbValue;

    StatusPedidoReserva(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() { return dbValue; }
    public static StatusPedidoReserva fromDbValue(String dbValue) {
        for (StatusPedidoReserva status : values()) {
            if (status.dbValue.equals(dbValue)) return status;
        }
        throw new IllegalArgumentException("Status inválido: " + dbValue);
    }
}

