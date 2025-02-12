package com.example.springserver.domain.caregiver.converter;

import com.example.springserver.domain.caregiver.entity.Caregiver;
import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDTO;
import com.example.springserver.domain.caregiver.dto.response.CaregiverResponseDTO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CaregiverConverter {

    // 날짜를 포맷하는 메서드
    private static String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    public static CaregiverResponseDTO.SignUpCaregiverResult toSignUpCaregiverResult(Caregiver caregiver){
        return CaregiverResponseDTO.SignUpCaregiverResult.builder()
                .caregiverId(caregiver.getId())
                .createAt(formatDateTime(caregiver.getCreatedAt()))
                .build();
    }

    //    Caregiver 객체를 만드는 작업 (클라이언트가 준 DTO to Entity)
    public static Caregiver toCaregiver(CaregiverRequestDTO.SignUpCaregiverReq request, BCryptPasswordEncoder bCryptPasswordEncoder){

        return Caregiver.builder()
                .username(request.getUsername())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .connect(request.getConnect())
                .car(request.getCar())
                .education(request.getEducation())
                .img(request.getImg())
                .intro(request.getIntro())
                .address(request.getAddress())
                .employmentStatus(request.getEmploymentStatus())
                .build();
    }
}
