package com.fsu.reservas_lab.entities.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

public enum TipoAprovacao {
    COORDENADOR_LAB("coordenador_lab"),
    COORDENADOR_CURSO("coordenador_curso"),
    REITORIA("reitoria");

    private final String dbValue;

    TipoAprovacao(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() { return dbValue; }
    public static TipoAprovacao fromDbValue(String dbValue) {
        for (TipoAprovacao tipo : values()) {
            if (tipo.dbValue.equals(dbValue)) return tipo;
        }
        throw new IllegalArgumentException("Tipo de aprovação inválido: " + dbValue);
    }
}

@Converter(autoApply = true)
class TipoAprovacaoConverter implements AttributeConverter<TipoAprovacao, String> {
    @Override
    public String convertToDatabaseColumn(TipoAprovacao tipo) {
        return tipo != null ? tipo.getDbValue() : null;
    }

    @Override
    public TipoAprovacao convertToEntityAttribute(String dbData) {
        return dbData != null ? TipoAprovacao.fromDbValue(dbData) : null;
    }
}