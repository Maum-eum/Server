package com.example.springserver.domain.center.dto.request;

import com.example.springserver.domain.center.entity.enums.ElderRate;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

import java.time.LocalDate;

public class ElderRequestDto {

    @Getter
    public static class CreateReqDto {

        private String name;
        private int gender;
        private LocalDate birth;
        @Enumerated(EnumType.STRING)
        private ElderRate rate; // 장기 요양 등급
        private String img;
        private int weight;
    }

    public static class JoinDto {

    }

}
