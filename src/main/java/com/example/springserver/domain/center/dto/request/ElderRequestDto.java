package com.example.springserver.domain.center.dto.request;

import com.example.springserver.domain.center.entity.enums.ElderRate;
import com.example.springserver.domain.center.entity.enums.Inmate;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

public class ElderRequestDto {

    @Getter
    public static class RequestDto { // 공통 필드

        private String name;
        private String centerName;
        private Integer gender;
        private LocalDate birth;
        private ElderRate rate; // 장기 요양 등급
        private List<Inmate> inmateTypes;
        private String imgUrl;
        private Integer weight;
        private boolean isNormal; // 증상 보유 여부
        private boolean hasShortTermMemoryLoss; // 단기 기억 장애 여부
        private boolean wandersOutside; // 집밖을 배회 여부
        private boolean actsLikeChild; // 어린아이 같은 행동 여부
        private boolean hasDelusions; // 망상 여부
        private boolean hasAggressiveBehavior; // 공격적인 행동 여부
    }

    @Getter
    public static class CreateRequestDto {

        private String name;
        private Integer gender;
        private LocalDate birth;
        private ElderRate rate; // 장기 요양 등급
        private List<Inmate> inmateTypes;
        private Integer weight;
        private boolean isNormal; // 증상 보유 여부
        private boolean hasShortTermMemoryLoss; // 단기 기억 장애 여부
        private boolean wandersOutside; // 집밖을 배회 여부
        private boolean actsLikeChild; // 어린아이 같은 행동 여부
        private boolean hasDelusions; // 망상 여부
        private boolean hasAggressiveBehavior; // 공격적인 행동 여부
    }

    @Getter
    public static class UpdateRequestDto {

        private String name;
        private String centerName;
        private ElderRate rate; // 장기 요양 등급
        private List<Inmate> inmateTypes;
        private String imgUrl;
        private Integer weight;
        private boolean isNormal; // 증상 보유 여부
        private boolean hasShortTermMemoryLoss; // 단기 기억 장애 여부
        private boolean wandersOutside; // 집밖을 배회 여부
        private boolean actsLikeChild; // 어린아이 같은 행동 여부
        private boolean hasDelusions; // 망상 여부
        private boolean hasAggressiveBehavior; // 공격적인 행동 여부
    }

}