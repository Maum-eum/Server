package com.example.springserver.converter.location;

import com.example.springserver.domain.entity.location.Location;
import com.example.springserver.domain.entity.location.Sido;
import com.example.springserver.domain.entity.location.Sigungu;
import com.example.springserver.dto.Location.LocationResponseDto.ResponseSidoDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.springserver.dto.Location.LocationResponseDto.*;

public class LocationConverter {

    private static String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    // Sido -> SidoDto
    public static ResponseSidoDto toResponseSidoDto(Sido sido){
        return ResponseSidoDto.builder()
                .sidoId(sido.getSidoId())
                .sidoName(sido.getSidoName())
                .build();
    }

    // Sigungu -> SigunguDto
    public static ResponseSigunguDto toResponseSigunguDto(Sigungu sigungu){
        return ResponseSigunguDto.builder()
                .sigunguId(sigungu.getSigungu_id())
                .sigunguName(sigungu.getSigungu_name())
                .build();
    }

    // Location -> LocationDto
    private static ResponseLocationDto toResponseLocationDto(Location location) {
        return ResponseLocationDto.builder()
                .location_id(location.getLocation_id())
                .sido_name(location.getSido_name())
                .sigungu_name(location.getSigungu_name())
                .dong_name(location.getDong_name())
                .address(location.getSido_name()+" "+location.getSigungu_name()+" "+location.getDong_name())
                .build();
    }

    // SidoList -> DtoSidoList
    public static List<ResponseSidoDto> toSidoListDto(List<Sido> sidoList) {
        return sidoList.stream()
                .map(LocationConverter::toResponseSidoDto)
                .collect(Collectors.toList());
    }

    // SigunguList -> DtoSigunguList
    public static List<ResponseSigunguDto> toSigunguListDto(List<Sigungu> sigunguList) {
        return sigunguList.stream()
                .map(LocationConverter::toResponseSigunguDto)
                .collect(Collectors.toList());
    }

    // LocationList-> DtoLocationList
    public static List<ResponseLocationDto> toLocationListDto(List<Location> locationList) {
        return locationList.stream()
                .map(LocationConverter::toResponseLocationDto)
                .collect(Collectors.toList());
    }


}
