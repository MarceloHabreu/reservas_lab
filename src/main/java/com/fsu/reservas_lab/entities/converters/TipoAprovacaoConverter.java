package com.fsu.reservas_lab.entities.converters;

import com.fsu.reservas_lab.entities.enums.TipoAprovacao;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)  // Aplica automaticamente a todas as propriedades do tipo TipoAprovacao
public class TipoAprovacaoConverter implements AttributeConverter<TipoAprovacao, String> {

    @Override
    public String convertToDatabaseColumn(TipoAprovacao attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getDbValue();  // Usa o valor customizado para o banco de dados
    }

    @Override
    public TipoAprovacao convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return TipoAprovacao.fromDbValue(dbData);  // Converte do banco de dados para o enum
    }
}