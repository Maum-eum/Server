package com.example.springserver.domain.center.converter;

import com.example.springserver.domain.center.entity.enums.Week;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class WeekConverter implements AttributeConverter<Week, Integer> {

    // 엔티티 → DB 저장 (Enum → Integer)
    @Override
    public Integer convertToDatabaseColumn(Week attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getBitMask();
    }

    // DB → 엔티티 반환 (Integer → Enum)
    @Override
    public Week convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return Week.fromBitMask(dbData);
    }
}