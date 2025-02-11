package com.example.springserver.dto.elder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ElderResponseDto {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreatDto {
        private Long elderId;
        private String name;
        private String createAt;
    }
}