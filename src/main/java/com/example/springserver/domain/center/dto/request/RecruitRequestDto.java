package com.example.springserver.domain.center.dto.request;

import com.example.springserver.domain.center.dto.RecruitConditionOptionInfo;
import com.example.springserver.domain.center.entity.enums.CareType;
import com.example.springserver.domain.center.entity.enums.Week;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;

import java.util.List;

public class RecruitRequestDto {

    @Getter
    public static class Request {
        private Long recruitLocationId;
        @JsonUnwrapped
        private RecruitConditionOptionInfo recruitConditionOptionInfo;
        private List<CareType> careTypes; // 근무 종류
        private List<TimeRequest> recruitTimes;
        private String detailRequiredService; // 추가 요청 사항
    }

    @Getter
    public static class TimeRequest {
        private Week dayOfWeek;
        private Long startTime;
        private Long endTime;
    }
}
