package com.example.springserver.domain.caregiver.cache;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkLocationCache {
    private Long workLocationId;
    private Long locationId;
    private String address;
}