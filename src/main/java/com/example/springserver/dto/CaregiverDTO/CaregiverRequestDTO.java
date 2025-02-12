package com.example.springserver.dto.CaregiverDTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class CaregiverRequestDTO {

    @Getter
    public static class SignUpCaregiverReq{

        @NotEmpty
        private String username;

        @NotEmpty
        private String password;

        @NotEmpty
        private String name;

        @NotEmpty
        private String connect;

        @NotNull
        private Boolean car;

        @NotNull
        private Boolean education;

        private String img;

        private String intro;

        private String address;

        private Boolean employmentStatus;

        public SignUpCaregiverReq() {
            this.car = false; // 기본값 false
            this.education = false;   // 기본값 false
        }
    }
}
