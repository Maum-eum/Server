package com.example.springserver.domain.center.converter;

import com.example.springserver.domain.center.dto.request.ElderRequestDto.CreateRequestDto;
import com.example.springserver.domain.center.dto.request.ElderRequestDto.RequestDto;
import com.example.springserver.domain.center.dto.request.ElderRequestDto.UpdateRequestDto;
import com.example.springserver.domain.center.dto.response.ElderResponseDto.*;
import com.example.springserver.domain.center.entity.Care;
import com.example.springserver.domain.center.entity.Center;
import com.example.springserver.domain.center.entity.Elder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
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
                .inmateTypes(request.getInmateTypes())
                .weight(request.getWeight())
                .isNormal(request.isNormal())
                .hasShortTermMemoryLoss(request.isHasShortTermMemoryLoss())
                .wandersOutside(request.isWandersOutside())
                .actsLikeChild(request.isActsLikeChild())
                .hasDelusions(request.isHasDelusions())
                .hasAggressiveBehavior(request.isHasAggressiveBehavior())
                .build();
    }

    public static Elder toSaveElder(CreateRequestDto dto, Center center, String imgUrl) {
        return Elder.builder()
                .center(center)
                .name(dto.getName())
                .gender(dto.getGender())
                .birth(dto.getBirth())
                .rate(dto.getRate())
                .inmateTypes(dto.getInmateTypes())
                .imgUrl(imgUrl)
                .weight(dto.getWeight())
                .isTemporarySave(dto.isTemporarySave())
                .isNormal(dto.isNormal())
                .hasShortTermMemoryLoss(dto.isHasShortTermMemoryLoss())
                .wandersOutside(dto.isWandersOutside())
                .actsLikeChild(dto.isActsLikeChild())
                .hasDelusions(dto.isHasDelusions())
                .hasAggressiveBehavior(dto.isHasAggressiveBehavior())
                .build();
    }

    public static Elder toUpdateElder(UpdateRequestDto dto, Center center) {
        return Elder.builder()
                .center(center)
                .name(dto.getName())
                .rate(dto.getRate())
                .inmateTypes(dto.getInmateTypes())
                .imgUrl(dto.getImgUrl())
                .weight(dto.getWeight())
                .isTemporarySave(false) // 업데이트가 완료되면 임시 저장 상태 변경
                .isNormal(dto.isNormal())
                .hasShortTermMemoryLoss(dto.isHasShortTermMemoryLoss())
                .wandersOutside(dto.isWandersOutside())
                .actsLikeChild(dto.isActsLikeChild())
                .hasDelusions(dto.isHasDelusions())
                .hasAggressiveBehavior(dto.isHasAggressiveBehavior())
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
                .careId(Optional.ofNullable(elder.getCareInfo())
                        .map(Care::getCareId) // careInfo가 존재하면 careId 반환
                        .orElse(null)) // 없으면 null 반환
                .elderId(elder.getElderId())
                .name(elder.getName())
                .gender(elder.getGender())
                .rate(elder.getRate())
                .inmateTypes(elder.getInmateTypes())
                .birth(elder.getBirth())
                .img(elder.getImgUrl())
                .weight(elder.getWeight())
                .isTemporarySave(elder.isTemporarySave())
                .isNormal(elder.isNormal())
                .hasShortTermMemoryLoss(elder.isHasShortTermMemoryLoss())
                .wandersOutside(elder.isWandersOutside())
                .actsLikeChild(elder.isActsLikeChild())
                .hasDelusions(elder.isHasDelusions())
                .hasAggressiveBehavior(elder.isHasAggressiveBehavior())
                .build();
    }


    public static UpdateResponseDto toUpdateResponseDto(Elder elder) {
        return UpdateResponseDto.builder()
                .elderId(elder.getElderId())
                .name(elder.getName())
                .rate(elder.getRate())
                .inmateTypes(elder.getInmateTypes())
                .img(elder.getImgUrl())
                .weight(elder.getWeight())
                .isTemporarySave(elder.isTemporarySave())
                .isNormal(elder.isNormal())
                .hasShortTermMemoryLoss(elder.isHasShortTermMemoryLoss())
                .wandersOutside(elder.isWandersOutside())
                .actsLikeChild(elder.isActsLikeChild())
                .hasDelusions(elder.isHasDelusions())
                .hasAggressiveBehavior(elder.isHasAggressiveBehavior())
                .build();
    }

    public static DeleteResponseDto toDeleteResponseDto(Elder elder) {
        return DeleteResponseDto.builder()
                .elderId(elder.getElderId())
                .name(elder.getName())
                .build();
    }

    public static List<ResponseDto> toListDto(List<Elder> elderList) {
        return elderList.stream()
                .map(ElderConverter::toResponseDto)  // ElderEntity를 ResponseDto로 변환
                .collect(Collectors.toList());
    }

    public static MatchElderResponseDto toMatchElderDto(Elder elder) {
        return MatchElderResponseDto.builder()
                .elderId(elder.getElderId())
                .name(elder.getName())
                .imgUrl(elder.getImgUrl())
                .build();
    }
}