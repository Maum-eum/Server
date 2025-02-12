package com.example.springserver.domain.location.dto.response;

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
        private Long locationId;
        private String dongName;
        private String sidoName;
        private String sigunguName;
        private String address;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseAddress{
        private String address;
    }
}
