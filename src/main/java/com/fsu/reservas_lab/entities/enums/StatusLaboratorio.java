package com.fsu.reservas_lab.entities.enums;

public enum StatusLaboratorio {
    DISPONIVEL("disponivel"),
    INDISPONIVEL("indisponivel");

    private final String dbValue;

    StatusLaboratorio(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    public static StatusLaboratorio fromDbValue(String dbValue) {
        for (StatusLaboratorio status : values()) {
            if (status.dbValue.equals(dbValue)) return status;
        }
        throw new IllegalArgumentException("Status inválido: " + dbValue);
    }
}
