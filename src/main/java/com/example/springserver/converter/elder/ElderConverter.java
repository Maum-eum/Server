package com.example.springserver.converter.elder;

import com.example.springserver.domain.entity.elder.Elder;
import com.example.springserver.dto.elder.ElderRequestDto.CreateReqDto;
import com.example.springserver.dto.elder.ElderResponseDto.CreatDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ElderConverter {
    private static String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    public static Elder toElder(CreateReqDto request) {

        return Elder.builder()
                .name(request.getName())
                .birth(request.getBirth())
                .gender(request.getGender())
                .rate(request.getRate())
                .weight(request.getWeight())
                .build();
    }

    public static CreatDto toCreateDto(Elder elder) {
        return CreatDto.builder()
                .elderId(elder.getId())
                .name(elder.getName())
                .createAt(formatDateTime(elder.getCreatedAt()))
                .build();
    }
}
