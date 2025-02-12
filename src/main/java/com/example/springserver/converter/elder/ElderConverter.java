package com.example.springserver.converter.elder;

import com.example.springserver.domain.entity.elder.ElderEntity;
import com.example.springserver.dto.elder.ElderRequestDto.CreateReqDto;
import com.example.springserver.dto.elder.ElderResponseDto;
import com.example.springserver.dto.elder.ElderResponseDto.CreateDto;
import com.example.springserver.dto.elder.ElderResponseDto.ResponseDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ElderConverter {
    private static String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    public static ElderEntity toElder(CreateReqDto request) {

        return ElderEntity.builder()
                .name(request.getName())
                .birth(request.getBirth())
                .gender(request.getGender())
                .rate(request.getRate())
                .weight(request.getWeight())
                .build();
    }

    public static CreateDto toCreateDto(ElderEntity elderEntity) {
        return CreateDto.builder()
                .elderId(elderEntity.getId())
                .name(elderEntity.getName())
                .createAt(formatDateTime(elderEntity.getCreatedAt()))
                .build();
    }

    // ElderEntity를 ResponseDto로 변환
    public static ResponseDto toResponseDto(ElderEntity elder) {
        return ResponseDto.builder()
                .elderId(elder.getId())
                .name(elder.getName())
                .gender(elder.getGender())
                .rate(elder.getRate())
                .birth(elder.getBirth())
                .img(elder.getImg())
                .weight(elder.getWeight())
                .build();
    }

    public static List<ResponseDto> toListDto(List<ElderEntity> elderList) {
        return elderList.stream()
                .map(ElderConverter::toResponseDto)  // ElderEntity를 ResponseDto로 변환
                .collect(Collectors.toList());
    }
}
