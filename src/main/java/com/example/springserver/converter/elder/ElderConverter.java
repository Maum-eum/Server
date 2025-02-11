package com.example.springserver.converter.elder;

import com.example.springserver.domain.entity.UserEntity;
import com.example.springserver.domain.entity.elder.ElderEntity;
import com.example.springserver.dto.UserResponseDTO;
import com.example.springserver.dto.elder.ElderRequestDto.CreateReqDto;
import com.example.springserver.dto.elder.ElderResponseDto.CreatDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public static CreatDto toCreateDto(ElderEntity elder) {
        return CreatDto.builder()
                .elderId(elder.getElderId())
                .name(elder.getName())
                .createAt(formatDateTime(elder.getCreatedAt()))
                .build();
    }
}
