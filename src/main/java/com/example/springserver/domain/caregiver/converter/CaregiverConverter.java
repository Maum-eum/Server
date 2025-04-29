package com.example.springserver.domain.caregiver.converter;

import com.example.springserver.domain.caregiver.dto.CaregiverBasicInfo;
import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDto;
import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDto.CertificateRequest;
import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDto.ExperienceRequest;
import com.example.springserver.domain.caregiver.dto.response.CaregiverResponseDto.*;
import com.example.springserver.domain.caregiver.entity.Caregiver;
import com.example.springserver.domain.caregiver.entity.Certificate;
import com.example.springserver.domain.caregiver.entity.Experience;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CaregiverConverter {

    // 날짜를 포맷하는 메서드
    private static String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    public static CaregiverSignupResponse toSignUpCaregiverResult(Caregiver caregiver){
        return CaregiverSignupResponse.builder()
                .caregiverId(caregiver.getId())
                .createAt(formatDateTime(caregiver.getCreatedAt()))
                .build();
    }

    public static CareGiverInfoResponse infoResponseDto(Caregiver caregiver){
        return CareGiverInfoResponse.builder()
                .basicInfo(
                        CaregiverBasicInfo.builder()
                                .username(caregiver.getUsername())
                                .name(caregiver.getName())
                                .contact(caregiver.getContact())
                                .car(caregiver.getCar())
                                .education(caregiver.getEducation())
                                .intro(caregiver.getIntro())
                                .address(caregiver.getAddress())
                                .build()
                )
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
    public static Caregiver toCaregiver(CaregiverRequestDto.CaregiverSignupRequest request, BCryptPasswordEncoder bCryptPasswordEncoder, String imgUrl){

        return Caregiver.builder()
                .username(request.getBasicInfo().getUsername())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .name(request.getBasicInfo().getName())
                .contact(request.getBasicInfo().getContact())
                .car(request.getBasicInfo().getCar())
                .education(request.getBasicInfo().getEducation())
                .intro(request.getBasicInfo().getIntro())
                .address(request.getBasicInfo().getAddress())
                .img(imgUrl)
                .employmentStatus(request.getEmploymentStatus())
                .build();
    }

    public static Experience toExperience(Caregiver caregiver, ExperienceRequest request){
        return Experience.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .duration(request.getDuration())
                .caregiver(caregiver)
                .build();
    }

    public static Certificate toCertificate(Caregiver caregiver, CertificateRequest request){
        return Certificate.builder()
                .certNum(request.getCertNum())
                .certRate(request.getCertRate())
                .certType(request.getCertType())
                .caregiver(caregiver)
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