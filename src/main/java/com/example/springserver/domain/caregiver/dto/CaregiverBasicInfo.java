package com.example.springserver.domain.caregiver.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CaregiverBasicInfo {
    private String username;
    @NotEmpty private String name;
    @NotEmpty private String contact;
    @NotNull private Boolean car;
    @NotNull private Boolean education;
    private String intro;
    private String address;
}