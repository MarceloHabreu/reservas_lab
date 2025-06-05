package com.fsu.reservas_lab.entities.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

public enum AreaLaboratorio {
    ADS("ads", "Análise e Desenvolvimento de Sistemas"),
    ARQUITETURA_URBANISMO("arquitetura_urbanismo", "Arquitetura e Urbanismo"),
    ENFERMAGEM("enfermagem", "Enfermagem"),
    RADIOLOGIA("radiologia", "Radiologia"),
    ADMINISTRACAO("administracao", "Administração"),
    BIOMEDICINA("biomedicina", "Biomedicina"),
    DESIGN_MODA("design_moda", "Design de Moda"),
    DIREITO("direito", "Direito"),
    EDUCACAO_FISICA("educacao_fisica", "Educação Física"),
    ENGENHARIA_CIVIL("engenharia_civil", "Engenharia Civil"),
    ENGENHARIA_ELETRICA("engenharia_eletrica", "Engenharia Elétrica"),
    ESTETICA_COSMETICA("estetica_cosmetica", "Estética e Cosmética"),
    FARMACIA("farmacia", "Farmácia"),
    ODONTOLOGIA("odontologia", "Odontologia");

    private final String dbValue;
    private final String nomeCompleto;

    AreaLaboratorio(String dbValue, String nomeCompleto) {
        this.dbValue = dbValue;
        this.nomeCompleto = nomeCompleto;
    }

    public String getDbValue() { return dbValue; }
    public String getNomeCompleto() { return nomeCompleto; }
    public static AreaLaboratorio fromDbValue(String dbValue) {
        for (AreaLaboratorio area : values()) {
            if (area.dbValue.equals(dbValue)) return area;
        }
        throw new IllegalArgumentException("Área inválida: " + dbValue);
    }
}

@Converter(autoApply = true)
class AreaLaboratorioConverter implements AttributeConverter<AreaLaboratorio, String> {
    @Override
    public String convertToDatabaseColumn(AreaLaboratorio area) {
        return area != null ? area.getDbValue() : null;
    }

    @Override
    public AreaLaboratorio convertToEntityAttribute(String dbData) {
        return dbData != null ? AreaLaboratorio.fromDbValue(dbData) : null;
    }
}