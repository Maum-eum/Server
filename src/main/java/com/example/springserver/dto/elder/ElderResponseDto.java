package com.example.springserver.dto.elder;

import com.example.springserver.domain.entity.elder.enums.ElderRate;
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
        private String createAt;
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

}