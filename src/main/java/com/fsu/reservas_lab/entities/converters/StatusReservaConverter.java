package com.fsu.reservas_lab.entities.converters;

import com.fsu.reservas_lab.entities.enums.StatusPedidoReserva;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
class StatusReservaConverter implements AttributeConverter<StatusPedidoReserva, String> {
    @Override
    public String convertToDatabaseColumn(StatusPedidoReserva status) {
        return status != null ? status.getDbValue() : null;
    }

    @Override
    public StatusPedidoReserva convertToEntityAttribute(String dbData) {
        return dbData != null ? StatusPedidoReserva.fromDbValue(dbData) : null;
    }
}
