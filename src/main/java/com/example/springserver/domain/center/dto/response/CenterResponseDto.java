package com.example.springserver.domain.center.dto.response;

import com.example.springserver.domain.center.dto.response.AdminResponseDto.AdminResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class CenterResponseDto {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CenterSearchDto {
        private Long centerId;

        private String centerName;

        private String centerLeaderName;

        private List<AdminResult> admins;

        private List<ElderResponseDto.ResponseDto> elders;

        private Boolean hasBathCar; // 목욕차량 보유 여부

        private String rate;

        private String intro;

        private String startTime;

        private String endTime;

        private String address;
    }
}
