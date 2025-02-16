package com.example.springserver.domain.match.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class MatchResponseDTO {

    @Getter
    @Builder
    public static class CaregiverRecommendedList{

        private List<CaregiverRecommendedRes> recommendedlist;
    }

    @Getter
    @Builder
    public static class CaregiverRecommendedRes{

        private Long caregiverId;

        private String name;

        private int persent;

        List<LocationRes> locationList;
    }

    @Getter
    @Builder
    public static class LocationRes{

        private Long workLocationId;

        private String locationName;

    }
}
