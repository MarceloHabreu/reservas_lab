package com.fsu.reservas_lab.entities.converters;

import com.fsu.reservas_lab.entities.enums.TipoUsuario;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoUsuarioConverter implements AttributeConverter<TipoUsuario, String> {
    @Override
    public String convertToDatabaseColumn(TipoUsuario tipo) {
        return tipo != null ? tipo.getDbValue() : null;
    }

    @Override
    public TipoUsuario convertToEntityAttribute(String dbData) {
        return dbData != null ? TipoUsuario.fromDbValue(dbData) : null;
    }
}
