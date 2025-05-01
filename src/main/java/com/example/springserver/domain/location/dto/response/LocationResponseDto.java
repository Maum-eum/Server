package com.example.springserver.domain.location.dto.response;

import lombok.Builder;
import lombok.Getter;

public class LocationResponseDto {

    @Getter
    @Builder
    public static class ResponseSidoDto{
        private Long sidoId;
        private String sidoName;
    }

    @Getter
    @Builder
    public static class ResponseSigunguDto{
        private Long sigunguId;
        private String sigunguName;
    }

    @Getter
    @Builder
    public static class ResponseLocationDto{
        private Long locationId;
        private String dongName;
        private String sidoName;
        private String sigunguName;
        private String address;
    }

    @Getter
    @Builder
    public static class ResponseAddress{
        private String address;
    }
}
