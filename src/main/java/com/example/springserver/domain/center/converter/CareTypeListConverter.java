package com.example.springserver.domain.center.converter;

import com.example.springserver.domain.center.entity.enums.CareType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter(autoApply = true)
public class CareTypeListConverter implements AttributeConverter<List<CareType>, String> {

    @Override
    public String convertToDatabaseColumn(List<CareType> attribute) {
        if (attribute == null || attribute.isEmpty()) return "";
        return attribute.stream().map(Enum::name).collect(Collectors.joining(","));
    }

    @Override
    public List<CareType> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return List.of();
        return Arrays.stream(dbData.split(",")).map(CareType::valueOf).collect(Collectors.toList());
    }
}
