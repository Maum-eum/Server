package com.example.springserver.domain.caregiver.converter;

import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDto.CertificateRequestDTO;
import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDto.ExperienceRequestDTO;
import com.example.springserver.domain.caregiver.dto.response.CaregiverResponseDto.*;
import com.example.springserver.domain.caregiver.entity.*;
import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDto;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CaregiverConverter {

    // 날짜를 포맷하는 메서드
    private static String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    public static SignUpCaregiverResponse toSignUpCaregiverResult(Caregiver caregiver){
        return SignUpCaregiverResponse.builder()
                .caregiverId(caregiver.getId())
                .createAt(formatDateTime(caregiver.getCreatedAt()))
                .build();
    }

    public static CareGiverInfoResponse infoResponseDto(Caregiver caregiver){
        return CareGiverInfoResponse.builder()
                .username(caregiver.getName())
                .contact(caregiver.getContact())
                .car(caregiver.getCar())
                .education(caregiver.getEducation())
                .into(caregiver.getIntro())
                .address(caregiver.getAddress())
                .employmentStatus(caregiver.getEmploymentStatus())
                .certificateResponseList(caregiver.getCertificates().stream()
                        .map(CaregiverConverter::toResponseCertificate)
                        .toList())
                .experienceResponseList(caregiver.getExperiences().stream()
                        .map(CaregiverConverter::toResponseExperience)
                        .toList())
                .img(caregiver.getImg())
                .build();
    }

    public static ExperienceResponse toResponseExperience(Experience experience) {
        return ExperienceResponse.builder()
                .duration(experience.getDuration())
                .title(experience.getTitle())
                .description(experience.getDescription())
                .build();
    }

    public static CertificateResponse toResponseCertificate(Certificate certificate) {
        return CertificateResponse.builder()
                .certNum(certificate.getCertNum())
                .certRate(certificate.getCertRate())
                .certType(certificate.getCertType())
                .build();
    }

    //    Caregiver 객체를 만드는 작업 (클라이언트가 준 DTO to Entity)
    public static Caregiver toCaregiver(CaregiverRequestDto.SignUpCaregiverReqDto request, BCryptPasswordEncoder bCryptPasswordEncoder, String imgUrl){

        return Caregiver.builder()
                .username(request.getUsername())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .contact(request.getContact())
                .car(request.getCar())
                .education(request.getEducation())
                .img(imgUrl)
                .intro(request.getIntro())
                .address(request.getAddress())
                .employmentStatus(request.getEmploymentStatus())
                .build();
    }

    public static Experience toExperience(Caregiver caregiver, ExperienceRequestDTO request){
        return Experience.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .duration(request.getDuration())
                .caregiver(caregiver)
                .build();
    }

    public static Certificate toCertificate(Caregiver caregiver, CertificateRequestDTO request){
        return Certificate.builder()
                .certNum(request.getCertNum())
                .certRate(request.getCertRate())
                .certType(request.getCertType())
                .caregiver(caregiver)
                .build();
    }

    public static RequestsListResponse toRequestListRes(List<WorkResponse> list) {
        return RequestsListResponse.builder()
                .list(list)
                .build();
    }

    public static MatchCaregiverResponse toMatchCaregiverDto(Caregiver caregiver) {
        return MatchCaregiverResponse.builder()
                .careGiverId(caregiver.getId())
                .contact(caregiver.getContact())
                .username(caregiver.getName())
                .img(caregiver.getImg())
                .build();
    }
}
