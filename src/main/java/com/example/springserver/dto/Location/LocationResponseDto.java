package com.example.springserver.dto.Location;

import lombok.*;

public class LocationResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseSidoDto{
        private Long sidoId;
        private String sidoName;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseSigunguDto{
        private Long sigunguId;
        private String sigunguName;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseLocationDto{
        private Long location_id;
        private String dong_name;
        private String sido_name;
        private String sigungu_name;
        private String address;
    }


}
