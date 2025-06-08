package com.fsu.reservas_lab.entities.enums;

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

