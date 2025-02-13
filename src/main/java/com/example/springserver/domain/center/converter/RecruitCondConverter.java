package com.example.springserver.domain.center.converter;

import com.example.springserver.domain.center.entity.RecruitCondition;
import com.example.springserver.domain.center.dto.request.RecruitCondRequestDto.CreateReqDto;
import com.example.springserver.domain.center.dto.response.RecruitCondResponseDto;
import com.example.springserver.domain.center.dto.response.RecruitCondResponseDto.CreateDto;
import com.example.springserver.domain.center.dto.response.RecruitCondResponseDto.ResponseDto;

public class RecruitCondConverter {

    public static RecruitCondition toRecruitCondition(CreateReqDto createReqDto) {
        return RecruitCondition.builder()
                .careType(createReqDto.getCareType())
                .desiredHourlyWage(createReqDto.getDesiredHourlyWage())
                .build();
    }

    public static CreateDto toCreateDto(RecruitCondition recruitCondition) {
        return RecruitCondResponseDto.CreateDto.builder()
                .recruitCondId(recruitCondition.getRecruitConditionId())
                .createAt(recruitCondition.getCreatedAt())
                .build();
    }

    public static ResponseDto toResponseDto(RecruitCondition recruitCondition) {
        return RecruitCondResponseDto.ResponseDto.builder()
                .recruitCondId(recruitCondition.getRecruitConditionId())
                .createAt(recruitCondition.getCreatedAt())
                .build();
    }
}
