package com.example.springserver.domain.center.dto.response;

import com.example.springserver.domain.center.dto.response.AdminResponseDTO.AdminResponseDto;
import com.example.springserver.domain.center.dto.response.ElderResponseDto.ResponseDto;
import com.example.springserver.domain.center.entity.Admin;
import com.example.springserver.domain.center.entity.Elder;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
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

        private List<AdminResponseDto> admins;

        private List<ResponseDto> elders;

        private Boolean hasBathCar; // 목욕차량 보유 여부

        private String rate;

        private String intro;

        private String startTime;

        private String endTime;

        private String address;
    }
}
