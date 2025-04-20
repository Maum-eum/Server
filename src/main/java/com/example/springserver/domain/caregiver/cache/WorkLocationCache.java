package com.example.springserver.domain.caregiver.cache;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkLocationCache implements Serializable {
    private Long workLocationId;
    private Long locationId;
    private String address;
}