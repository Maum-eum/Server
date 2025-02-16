package com.example.springserver.domain.center.dto.response;

import com.example.springserver.domain.center.entity.enums.ElderRate;
import com.example.springserver.domain.center.entity.enums.Inmate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

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
        private String centerName;
        private int gender;
        private LocalDate birth;
        private List<Inmate> inmateTypes;
        private ElderRate rate;
        private String img;
        private Integer weight;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateResponseDto {
        private Long elderId;
        private String name;
        private String centerName;
        private ElderRate rate;
        private List<Inmate> inmateTypes;
        private String img;
        private Integer weight;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteResponseDto {
        private Long elderId;
        private String name;
    }

}