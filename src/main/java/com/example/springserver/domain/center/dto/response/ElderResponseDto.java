package com.example.springserver.domain.center.dto.response;

import com.example.springserver.domain.center.entity.enums.ElderRate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class ElderResponseDto {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateDto {
        private Long elderId;
        private String name;
        private int gender;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseDto {
        private Long elderId;
        private String name;
        private int gender;
        private ElderRate rate;
        private LocalDate birth;
        private String img;
        private Integer weight;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateResultDto {
        private Long elderId;
        private String name;
        private ElderRate rate;
        private String img;
        private Integer weight;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteResultDto {
        private Long elderId;
        private String name;
    }

}