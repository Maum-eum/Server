package com.example.springserver.dto.AdminDTO;

import com.example.springserver.validation.annotation.GenderValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
