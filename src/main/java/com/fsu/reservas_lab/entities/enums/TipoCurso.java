package com.fsu.reservas_lab.entities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoCurso {
    ADS("ads", "Análise e Desenvolvimento de Sistemas"),
    ARQUITETURA_URBANISMO("arquitetura_urbanismo", "Arquitetura e Urbanismo"),
    ENFERMAGEM("enfermagem", "Enfermagem"),
    RADIOLOGIA("radiologia", "Radiologia"),
    ADMINISTRACAO("administracao", "Administração"),
    BIOMEDICINA("biomedicina", "Biomedicina"),
    DESIGN_MODA("design_moda", "Design de Moda"),
    DIREITO("direito", "Direito"),
    EDUCACAO_FISICA("educ_fisica", "Educação Física"),
    ENGENHARIA_CIVIL("eng_civil", "Engenharia Civil"),
    ENGENHARIA_ELETRICA("eng_eletrica", "Engenharia Elétrica"),
    ESTETICA_COSMETICA("estetica_cosmetica", "Estética e Cosmética"),
    FARMACIA("farmacia", "Farmácia"),
    ODONTOLOGIA("odontologia", "Odontologia");

    private final String dbValue;
    private final String nomeCompleto;

    TipoCurso(String dbValue, String nomeCompleto) {
        this.dbValue = dbValue;
        this.nomeCompleto = nomeCompleto;
    }

    public String getDbValue() { return dbValue; }
    public String getNomeCompleto() { return nomeCompleto; }

    @JsonCreator
    public static TipoCurso fromDbValue(String dbValue) {
        for (TipoCurso tipo : values()) {
            if (tipo.dbValue.equals(dbValue)) return tipo;
        }
        throw new IllegalArgumentException("Curso inválido: " + dbValue);
    }

    @JsonValue
    public String toValue(){
        return dbValue;
    }
}

