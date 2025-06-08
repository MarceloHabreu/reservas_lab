package com.fsu.reservas_lab.entities.converters;

import com.fsu.reservas_lab.entities.enums.TipoAprovacao;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

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
