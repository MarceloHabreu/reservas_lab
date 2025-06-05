package com.fsu.reservas_lab.entities.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

public enum TipoUsuario {
    PROFESSOR(1, "professor"),
    COORDENADOR_LAB(2, "coordenador_lab"),
    COORDENADOR_CURSO(3, "coordenador_curso"),
    TECNICO(4, "tecnico"),
    REITORIA(5, "reitoria"),
    AUDITOR(6, "auditor");

    private final int codigo;
    private final String dbValue;

    TipoUsuario(int codigo, String dbValue) {
        this.codigo = codigo;
        this.dbValue = dbValue;
    }

    public int getCodigo() { return codigo; }
    public String getDbValue() { return dbValue; }
    public static TipoUsuario fromDbValue(String dbValue) {
        for (TipoUsuario tipo : values()) {
            if (tipo.dbValue.equals(dbValue)) return tipo;
        }
        throw new IllegalArgumentException("Tipo inválido: " + dbValue);
    }
}

@Converter(autoApply = true)
class TipoUsuarioConverter implements AttributeConverter<TipoUsuario, String> {
    @Override
    public String convertToDatabaseColumn(TipoUsuario tipo) {
        return tipo != null ? tipo.getDbValue() : null;
    }

    @Override
    public TipoUsuario convertToEntityAttribute(String dbData) {
        return dbData != null ? TipoUsuario.fromDbValue(dbData) : null;
    }
}