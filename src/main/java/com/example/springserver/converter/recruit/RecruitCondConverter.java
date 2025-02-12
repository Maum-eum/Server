package com.example.springserver.converter.recruit;

import com.example.springserver.domain.entity.recruit.RecruitCondition;
import com.example.springserver.dto.recruitCond.RecruitCondRequestDto.CreateReqDto;
import com.example.springserver.dto.recruitCond.RecruitCondResponseDto;
import com.example.springserver.dto.recruitCond.RecruitCondResponseDto.CreateDto;
import com.example.springserver.dto.recruitCond.RecruitCondResponseDto.ResponseDto;

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
