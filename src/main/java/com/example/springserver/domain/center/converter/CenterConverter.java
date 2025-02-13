package com.example.springserver.domain.center.converter;

import com.example.springserver.domain.center.dto.response.CenterResponseDto.CenterSearchDto;
import com.example.springserver.domain.center.entity.Center;

import java.util.stream.Collectors;

public class CenterConverter {
    public static CenterSearchDto toSearchDto(Center center) {
        return CenterSearchDto.builder()
                .centerId(center.getCenterId())
                .centerName(center.getCenterName())
                .address(center.getAddress())
                .rate(center.getRate())
                .hasBathCar(center.getHasBathCar())

                .elders(center.getElders().stream()
                        .map(ElderConverter::toResponseDto)
                        .collect(Collectors.toList()))

                .admins(center.getAdmins().stream()
                        .map(AdminConverter::toResponseDto)
                        .collect(Collectors.toList()))

                .build();
    }
}
