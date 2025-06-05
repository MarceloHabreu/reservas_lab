package com.fsu.reservas_lab.entities.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

public enum StatusReserva {
    PENDENTE("pendente"),
    APROVADO_LABORATORIO("aprovado_laboratorio"),
    APROVADO_CURSO("aprovado_curso"),
    APROVADO("aprovado"),
    REJEITADA("rejeitada");

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

@Converter(autoApply = true)
class StatusReservaConverter implements AttributeConverter<StatusReserva, String> {
    @Override
    public String convertToDatabaseColumn(StatusReserva status) {
        return status != null ? status.getDbValue() : null;
    }

    @Override
    public StatusReserva convertToEntityAttribute(String dbData) {
        return dbData != null ? StatusReserva.fromDbValue(dbData) : null;
    }
}
