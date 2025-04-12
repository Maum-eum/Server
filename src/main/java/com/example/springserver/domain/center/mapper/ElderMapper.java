package com.example.springserver.domain.center.mapper;

import com.example.springserver.domain.center.dto.request.ElderRequestDto;
import com.example.springserver.domain.center.entity.Elder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ElderMapper {

    void updateElderFromDto(@MappingTarget Elder elder, ElderRequestDto.RequestDto dto);
}