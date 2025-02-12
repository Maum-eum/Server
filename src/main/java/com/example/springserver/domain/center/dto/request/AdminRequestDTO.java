package com.example.springserver.domain.center.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

public class AdminRequestDTO {

    @Getter
    public static class SignUpAdminReq{

        @NotEmpty
        private String username;

        @NotEmpty
        private String password;

        @NotEmpty
        private String name;

        @NotEmpty
        private String connect;

        @NotEmpty
        private String centerName;
    }
}
