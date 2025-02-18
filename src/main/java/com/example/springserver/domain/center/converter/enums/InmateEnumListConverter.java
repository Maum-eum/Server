package com.example.springserver.domain.center.converter.enums;

import com.example.springserver.domain.center.entity.enums.Inmate;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter(autoApply = true)
public class InmateEnumListConverter implements AttributeConverter<List<Inmate>, String> {

    @Override
    public String convertToDatabaseColumn(List<Inmate> attribute) {
        return (attribute == null || attribute.isEmpty())
                ? null : attribute.stream().map(Enum::name).collect(Collectors.joining(","));
    }

    @Override
    public List<Inmate> convertToEntityAttribute(String dbData) {
        return (dbData == null || dbData.isBlank())
                ? List.of() : Arrays.stream(dbData.split(","))
                .map(String::trim) // 공백 제거
                .map(Inmate::valueOf)
                .collect(Collectors.toList());
    }
}
