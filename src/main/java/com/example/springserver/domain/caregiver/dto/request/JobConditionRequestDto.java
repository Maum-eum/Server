package com.example.springserver.domain.caregiver.dto.request;

import com.example.springserver.domain.caregiver.dto.JobConditionOptionInfo;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;

import java.util.List;

public class JobConditionRequestDto {

    @Getter
    public static class Request {
        @JsonUnwrapped
        private JobConditionOptionInfo jobConditionOptionInfo;
        private List<LocationRequest> locationRequestList;
    }

    @Getter
    public static class LocationRequest {
        private Long workLocationId;
        private Long locationId;
    }
}
