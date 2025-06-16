package com.fsu.reservas_lab.entities.converters;

import com.fsu.reservas_lab.entities.enums.StatusPedidoReserva;
import com.fsu.reservas_lab.entities.enums.StatusReserva;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusReservaConverter implements AttributeConverter<StatusReserva, String> {

    @Override
    public String convertToDatabaseColumn(StatusReserva status) {
        return status == null ? null : status.getDbValue();
    }

    @Override
    public StatusReserva convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return StatusReserva.fromDbValue(dbData);
    }
}
