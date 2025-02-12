package com.example.springserver.domain.center.converter;

import com.example.springserver.domain.center.entity.Admin;
import com.example.springserver.domain.center.entity.Center;
import com.example.springserver.domain.center.dto.request.AdminRequestDTO;
import com.example.springserver.domain.center.dto.response.AdminResponseDTO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdminConverter {

    // 날짜를 포맷하는 메서드
    private static String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    public static AdminResponseDTO.SignUpAdminResult toSignUpAdminResult(Admin admin){
        return AdminResponseDTO.SignUpAdminResult.builder()
                .adminId(admin.getId())
                .createAt(formatDateTime(admin.getCreatedAt()))
                .build();
    }

    //    Admin 객체를 만드는 작업 (클라이언트가 준 DTO to Entity)
    public static Admin toAdmin(AdminRequestDTO.SignUpAdminReq request, BCryptPasswordEncoder bCryptPasswordEncoder, Center center){

        return Admin.builder()
                .username(request.getUsername())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .connect(request.getConnect())
                .center(center)
                .build();
    }
}
