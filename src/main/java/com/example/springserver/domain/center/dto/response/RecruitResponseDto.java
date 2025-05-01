package com.example.springserver.domain.center.dto.response;

import com.example.springserver.domain.center.dto.RecruitConditionOptionInfo;
import com.example.springserver.domain.center.entity.enums.CareType;
import com.example.springserver.domain.center.entity.enums.Week;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class RecruitResponseDto {

    @Builder
    @Getter
    public static class Response {
        private Long recruitConditionId;
        private Long elderId;
        @JsonUnwrapped
        private RecruitConditionOptionInfo recruitConditionOptionInfo;
        private List<CareType> careTypes; // 근무 종류
        private Long recruitLocation;
        private String address;
        private List<TimeResponse> recruitTimes;
        private String detailRequiredService; // 추가 요청 사항
    }

    @Builder
    @Getter
    @NoArgsConstructor // Json 역직렬화용
    @AllArgsConstructor
    public static class TimeResponse {
        private Week dayOfWeek;
        private Long startTime;
        private Long endTime;
    }
}
