package com.example.springserver.domain.center.dto.request;

import com.example.springserver.domain.center.entity.Center;
import com.example.springserver.domain.center.entity.enums.ElderRate;
import lombok.Getter;

import java.time.LocalDate;

public class ElderRequestDto {

    @Getter
    public static class RequestDto { // 공통 필드

        private String name;
        private Center cetnter;
        private Integer gender;
        private LocalDate birth;
        private ElderRate rate; // 장기 요양 등급
        private String imgUrl;
        private int weight;
    }

    @Getter
    public static class CreateRequestDto {

        private String name;
        private Integer gender;
        private LocalDate birth;
        private ElderRate rate; // 장기 요양 등급
        private String imgUrl;
        private int weight;
    }

    @Getter
    public static class UpdateRequestDto {

        private String name;
        private ElderRate rate; // 장기 요양 등급
        private String imgUrl;
        private int weight;
    }

}