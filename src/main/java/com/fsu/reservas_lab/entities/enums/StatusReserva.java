package com.fsu.reservas_lab.entities.enums;

public enum StatusReserva {
    ATIVA("ativa"),
    CONCLUIDA("concluida"),
    PENDENTE("pendente"),
    CANCELADA("cancelada");

    private final String dbValue;

    StatusReserva(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() { return dbValue; }

    public static StatusReserva fromDbValue(String dbValue) {
        for (StatusReserva status : values()) {
            if (status.dbValue.equals(dbValue)) return status;
        }
        throw new IllegalArgumentException("Status inválido: " + dbValue);
    }
}