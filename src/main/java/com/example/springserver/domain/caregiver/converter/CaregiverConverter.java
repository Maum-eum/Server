package com.example.springserver.domain.caregiver.converter;

import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDTO.CertificateRequestDTO;
import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDTO.ExperienceRequestDTO;
import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDTO.JobConditionRequestDTO;
import com.example.springserver.domain.caregiver.dto.response.CaregiverResponseDTO.*;
import com.example.springserver.domain.caregiver.entity.*;
import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDTO;

import com.example.springserver.domain.location.entity.Location;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CaregiverConverter {

    // 날짜를 포맷하는 메서드
    private static String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    public static SignUpCaregiverResult toSignUpCaregiverResult(Caregiver caregiver){
        return SignUpCaregiverResult.builder()
                .caregiverId(caregiver.getId())
                .createAt(formatDateTime(caregiver.getCreatedAt()))
                .build();
    }

    public static CareGiverInfoResponseDTO infoResponseDto(Caregiver caregiver){
        return CareGiverInfoResponseDTO.builder()
                .name(caregiver.getName())
                .contact(caregiver.getContact())
                .car(caregiver.getCar())
                .education(caregiver.getEducation())
                .intro(caregiver.getIntro())
                .address(caregiver.getAddress())
                .certificateResponseDTOList(caregiver.getCertificates().stream()
                        .map(CaregiverConverter::toResponseCertificate)
                        .toList())
                .experienceResponseDTOList(caregiver.getExperiences().stream()
                        .map(CaregiverConverter::toResponseExperience)
                        .toList())
                .img(caregiver.getImg())
                .build();
    }

    public static ExperienceResponseDTO toResponseExperience(Experience experience) {
        return ExperienceResponseDTO.builder()
                .duration(experience.getDuration())
                .title(experience.getTitle())
                .description(experience.getDescription())
                .build();
    }

    public static CertificateResponseDTO toResponseCertificate(Certificate certificate) {
        return CertificateResponseDTO.builder()
                .certNum(certificate.getCertNum())
                .certRate(certificate.getCertRate())
                .certType(certificate.getCertType())
                .build();
    }


    //    Caregiver 객체를 만드는 작업 (클라이언트가 준 DTO to Entity)
    public static Caregiver toCaregiver(CaregiverRequestDTO.SignUpCaregiverReq request, BCryptPasswordEncoder bCryptPasswordEncoder){

        return Caregiver.builder()
                .username(request.getUsername())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .contact(request.getContact())
                .car(request.getCar())
                .education(request.getEducation())
                .img(request.getImg())
                .intro(request.getIntro())
                .address(request.getAddress())
                .employmentStatus(request.getEmploymentStatus())
                .build();
    }


    public static Experience toExperience( ExperienceRequestDTO request){
        return Experience.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .duration(request.getDuration())
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

    public static Certificate toCertificate(CertificateRequestDTO request){
        return Certificate.builder()
                .certNum(request.getCertNum())
                .certRate(request.getCertRate())
                .certType(request.getCertType())
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

    public static JobCondition toJobCondition(JobConditionRequestDTO request,Caregiver user, List<WorkTime> time, List<WorkLocation> location) {
        return JobCondition.builder()
                .bathingAssist(request.getBathingAssist())
                .catheterOrStomaCare(request.getCatheterOrStomaCare())
                .diaperCare(request.getDiaperCare())
                .cleaningLaundryAssist(request.getCleaningLaundryAssist())
                .selfToileting(request.getSelfToileting())
                .selfFeeding(request.getSelfFeeding())
                .cognitiveStimulation(request.getCognitiveStimulation())
                .cookingAssistance(request.getCookingAssistance())
                .desiredHourlyWage(request.getDesiredHourlyWage())
                .emotionalSupport(request.getEmotionalSupport())
                .enteralNutritionSupport(request.getEnteralNutritionSupport())
                .exerciseSupport(request.getExerciseSupport())
                .hospitalAccompaniment(request.getHospitalAccompaniment())
                .flexibleSchedule(request.getFlexibleSchedule())
                .mealPreparation(request.getMealPreparation())
                .immobile(request.getImmobile())
                .occasionalToiletingAssist(request.getOccasionalToiletingAssist())
                .mobilityAssist(request.getMobilityAssist())
                .wheelchairAssist(request.getWheelchairAssist())
                .independentMobility(request.getIndependentMobility())
                .caregiver(user)
                .workTimes(time)
                .workLocations(location)
                .build();
    }

}
