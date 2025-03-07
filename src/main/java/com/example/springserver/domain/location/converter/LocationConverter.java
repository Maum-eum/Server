package com.example.springserver.domain.location.converter;

import com.example.springserver.domain.location.entity.Location;
import com.example.springserver.domain.location.entity.Sido;
import com.example.springserver.domain.location.entity.Sigungu;
import com.example.springserver.domain.location.dto.response.LocationResponseDto.ResponseSidoDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.springserver.domain.location.dto.response.LocationResponseDto.*;

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
                .sigunguId(sigungu.getSigunguId())
                .sigunguName(sigungu.getSigunguName())
                .build();
    }

    // Location -> LocationDto
    private static ResponseLocationDto toResponseLocationDto(Location location) {
        return ResponseLocationDto.builder()
                .locationId(location.getLocationId())
                .sidoName(location.getSidoName())
                .sigunguName(location.getSigunguName())
                .dongName(location.getDongName())
                .address(location.getSidoName()+" "+location.getSigunguName()+" "+location.getDongName())
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


    // String -> ResponseAddress
    public static ResponseAddress toResponseAddress(String address) {
        return ResponseAddress.builder()
                .address(address)
                .build();
    }
}
