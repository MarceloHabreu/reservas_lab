package com.fsu.reservas_lab.entities.converters;

import com.fsu.reservas_lab.entities.enums.StatusReserva;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

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
