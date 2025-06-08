package com.fsu.reservas_lab.entities.converters;

import com.fsu.reservas_lab.entities.enums.TipoCurso;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
class TipoCursoConverter implements AttributeConverter<TipoCurso, String> {
    @Override
    public String convertToDatabaseColumn(TipoCurso area) {
        return area != null ? area.getDbValue() : null;
    }

    @Override
    public TipoCurso convertToEntityAttribute(String dbData) {
        return dbData != null ? TipoCurso.fromDbValue(dbData) : null;
    }
}
