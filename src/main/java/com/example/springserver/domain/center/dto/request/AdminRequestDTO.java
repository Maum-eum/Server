package com.example.springserver.domain.center.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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

    @Getter
    public static class UpdateAdminReq{

        @Size(min = 1, message = "name은 비워둘 수 없습니다.")
        private String name;

        @Size(min = 1, message = "connect는 비워둘 수 없습니다.")
        private String connect;
    }
}
