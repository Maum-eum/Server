package com.example.springserver.dto.CaregiverDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CaregiverResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignUpCaregiverResult {
        private Long caregiverId;
        private String createAt;
    }
}
