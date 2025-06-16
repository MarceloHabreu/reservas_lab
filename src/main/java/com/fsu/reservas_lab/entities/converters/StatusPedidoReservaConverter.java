package com.fsu.reservas_lab.entities.converters;

import com.fsu.reservas_lab.entities.enums.StatusPedidoReserva;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusPedidoReservaConverter implements AttributeConverter<StatusPedidoReserva, String> {

    @Override
    public String convertToDatabaseColumn(StatusPedidoReserva status) {
        return status == null ? null : status.getDbValue(); // aqui usa o valor customizado
    }

    @Override
    public StatusPedidoReserva convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return StatusPedidoReserva.fromDbValue(dbData); // faz a conversão baseada no dbValue
    }
}

