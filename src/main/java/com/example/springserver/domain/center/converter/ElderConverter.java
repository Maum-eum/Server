package com.example.springserver.domain.center.converter;

import com.example.springserver.domain.center.dto.request.ElderRequestDto.RequestDto;
import com.example.springserver.domain.center.dto.request.ElderRequestDto.UpdateRequestDto;
import com.example.springserver.domain.center.dto.response.ElderResponseDto.DeleteResultDto;
import com.example.springserver.domain.center.dto.response.ElderResponseDto.UpdateResultDto;
import com.example.springserver.domain.center.entity.Center;
import com.example.springserver.domain.center.entity.Elder;
import com.example.springserver.domain.center.dto.request.ElderRequestDto.CreateRequestDto;
import com.example.springserver.domain.center.dto.response.ElderResponseDto.CreateDto;
import com.example.springserver.domain.center.dto.response.ElderResponseDto.ResponseDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ElderConverter {
    private static String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    public static Elder toElder(RequestDto request) {
        return Elder.builder()
                .name(request.getName())
                .birth(request.getBirth())
                .gender(request.getGender())
                .rate(request.getRate())
                .weight(request.getWeight())
                .build();
    }

    public static Elder toSaveElder(CreateRequestDto dto, boolean isTemporary, Center center) {
        return Elder.builder()
                .center(center)
                .name(dto.getName())
                .gender(dto.getGender())
                .birth(dto.getBirth())
                .rate(dto.getRate())
                .imgUrl(dto.getImgUrl())
                .weight(dto.getWeight())
                .isTemporary(isTemporary)
                .build();
    }

    public static Elder toUpdateElder(UpdateRequestDto dto, Center center) {
        return Elder.builder()
                .center(center)
                .name(dto.getName())
                .rate(dto.getRate())
                .imgUrl(dto.getImgUrl())
                .weight(dto.getWeight())
                .isTemporary(false) // 업데이트가 완료되면 임시 저장 상태 변경
                .build();
    }

    public static CreateDto toCreateDto(Elder elder) {
        return CreateDto.builder()
                .elderId(elder.getElderId())
                .name(elder.getName())
                .build();
    }

    // ElderEntity를 ResponseDto로 변환
    public static ResponseDto toResponseDto(Elder elder) {
        return ResponseDto.builder()
                .elderId(elder.getElderId())
                .name(elder.getName())
                .gender(elder.getGender())
                .rate(elder.getRate())
                .birth(elder.getBirth())
                .img(elder.getImgUrl())
                .weight(elder.getWeight())
                .build();
    }

    public static UpdateResultDto toUpdateResponseDto(Elder elder) {
        return UpdateResultDto.builder()
                .elderId(elder.getElderId())
                .name(elder.getName())
                .rate(elder.getRate())
                .img(elder.getImgUrl())
                .weight(elder.getWeight())
                .build();
    }

    public static DeleteResultDto toDeleteResponseDto(Elder elder) {
        return DeleteResultDto.builder()
                .elderId(elder.getElderId())
                .name(elder.getName())
                .build();
    }

    public static List<ResponseDto> toListDto(List<Elder> elderList) {
        return elderList.stream()
                .map(ElderConverter::toResponseDto)  // ElderEntity를 ResponseDto로 변환
                .collect(Collectors.toList());
    }
}