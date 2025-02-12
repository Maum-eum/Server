package com.example.springserver.dto.recruitCond;

import com.example.springserver.domain.entity.recruit.enums.CareType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class RecruitCondResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateDto {
        private Long recruitCondId;
        private LocalDateTime createAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseDto {
        private Long recruitCondId;
        private String elderName;
        private CareType careType;
        private LocalDateTime createAt;
    }
}
