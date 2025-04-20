package com.example.springserver.domain.caregiver.converter;

import com.example.springserver.domain.caregiver.dto.request.JobConditionRequestDto;
import com.example.springserver.domain.caregiver.dto.response.JobConditionResponseDto;
import com.example.springserver.domain.caregiver.dto.response.JobConditionResponseDto.CertificateResponseDTO;
import com.example.springserver.domain.caregiver.dto.response.JobConditionResponseDto.ExperienceResponseDTO;
import com.example.springserver.domain.caregiver.dto.response.JobConditionResponseDto.JobConditionResponseDTO;
import com.example.springserver.domain.caregiver.entity.*;
import com.example.springserver.global.utils.FormatUtils;
import com.example.springserver.service.location.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JobConditionConverter {

    private static LocationService locationService;

    @Autowired
    public void setLocationService(LocationService locationService) { // ✅ 정적 필드에 주입
        JobConditionConverter.locationService = locationService;
    }

    public static JobConditionResponseDTO tojobConditionResponseDTO(JobCondition saved){
        return JobConditionResponseDTO.builder()
                .jobConditionId(saved.getId())
                .bathingAssist(saved.getBathingAssist())
                .catheterOrStomaCare(saved.getCatheterOrStomaCare())
                .diaperCare(saved.getDiaperCare())
                .cleaningLaundryAssist(saved.getCleaningLaundryAssist())
                .selfToileting(saved.getSelfToileting())
                .selfFeeding(saved.getSelfFeeding())
                .cognitiveStimulation(saved.getCognitiveStimulation())
                .cookingAssistance(saved.getCookingAssistance())
                .desiredHourlyWage(saved.getDesiredHourlyWage())
                .emotionalSupport(saved.getEmotionalSupport())
                .enteralNutritionSupport(saved.getEnteralNutritionSupport())
                .exerciseSupport(saved.getExerciseSupport())
                .hospitalAccompaniment(saved.getHospitalAccompaniment())
                .flexibleSchedule(saved.getFlexibleSchedule())
                .mealPreparation(saved.getMealPreparation())
                .immobile(saved.getImmobile())
                .occasionalToiletingAssist(saved.getOccasionalToiletingAssist())
                .mobilityAssist(saved.getMobilityAssist())
                .wheelchairAssist(saved.getWheelchairAssist())
                .independentMobility(saved.getIndependentMobility())
                .dayOfWeek(Integer.toBinaryString(saved.getDayOfWeek()))
                .startTime(saved.getStartTime())
                .endTime(saved.getEndTime())
                .locationResponseDtoList(toListResponseDto(saved.getWorkLocations()))
                .caregiverId(saved.getCaregiver().getId())
                .build();
    }

    public static JobConditionResponseDto.DetailJobConditionResponseDTO toDetailJobConditionResponseDto(Caregiver caregiver, JobCondition saved) {
        return JobConditionResponseDto.DetailJobConditionResponseDTO.builder()
                .name(caregiver.getName())
                .contact(caregiver.getContact())
                .car(caregiver.getCar())
                .education(caregiver.getEducation())
                .intro(caregiver.getIntro())
                .address(caregiver.getAddress())
                .employmentStatus(caregiver.getEmploymentStatus())
                .certificateResponseDTOList(caregiver.getCertificates().stream()
                        .map(JobConditionConverter::toResponseCertificate)
                        .toList())
                .experienceResponseDTOList(caregiver.getExperiences().stream()
                        .map(JobConditionConverter::toResponseExperience)
                        .toList())
                .img(caregiver.getImg())
                .jobConditionId(saved.getId())
                .bathingAssist(saved.getBathingAssist())
                .catheterOrStomaCare(saved.getCatheterOrStomaCare())
                .diaperCare(saved.getDiaperCare())
                .cleaningLaundryAssist(saved.getCleaningLaundryAssist())
                .selfToileting(saved.getSelfToileting())
                .selfFeeding(saved.getSelfFeeding())
                .cognitiveStimulation(saved.getCognitiveStimulation())
                .cookingAssistance(saved.getCookingAssistance())
                .desiredHourlyWage(saved.getDesiredHourlyWage())
                .emotionalSupport(saved.getEmotionalSupport())
                .enteralNutritionSupport(saved.getEnteralNutritionSupport())
                .exerciseSupport(saved.getExerciseSupport())
                .hospitalAccompaniment(saved.getHospitalAccompaniment())
                .flexibleSchedule(saved.getFlexibleSchedule())
                .mealPreparation(saved.getMealPreparation())
                .immobile(saved.getImmobile())
                .occasionalToiletingAssist(saved.getOccasionalToiletingAssist())
                .mobilityAssist(saved.getMobilityAssist())
                .wheelchairAssist(saved.getWheelchairAssist())
                .independentMobility(saved.getIndependentMobility())
                .dayOfWeek(FormatUtils.toStringDayOfWeek(saved.getDayOfWeek()))
                .startTime(saved.getStartTime())
                .endTime(saved.getEndTime())
                .locationRequestDTOList(saved.getWorkLocations().stream()
                        .map(dto -> JobConditionResponseDto.LocationResponseDTO.builder()
                                .workLocationId(dto.getId())
                                .locationName(locationService.getLocation(dto.getLocation().getLocationId()))
                                .build()
                        )
                        .toList())
                .build();
    }

    private static List<JobConditionResponseDto.LocationResponseDTO> toListResponseDto(List<WorkLocation> workLocations) {
        return workLocations.stream()
                .map(JobConditionConverter::toWorkLocationResponseDto)
                .collect(Collectors.toList());
    }

    private static JobConditionResponseDto.LocationResponseDTO toWorkLocationResponseDto(WorkLocation location) {

        return JobConditionResponseDto.LocationResponseDTO.builder()
                .workLocationId(location.getId())
                .locationName(locationService.getLocation(location.getLocation().getLocationId()))
                .build();
    }

    public static CertificateResponseDTO toResponseCertificate(Certificate certificate) {
        return CertificateResponseDTO.builder()
                .certNum(certificate.getCertNum())
                .certRate(certificate.getCertRate())
                .certType(certificate.getCertType())
                .build();
    }

    public static ExperienceResponseDTO toResponseExperience(Experience experience) {
        return ExperienceResponseDTO.builder()
                .duration(experience.getDuration())
                .title(experience.getTitle())
                .description(experience.getDescription())
                .build();
    }

    public static JobCondition from(Caregiver user, JobConditionRequestDto.JobConditionReqDto request) {
        return JobCondition.builder()
                .caregiver(user)
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
                .dayOfWeek(FormatUtils.toIntegerDayOfWeek(request.getDayOfWeek()))
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .workLocations(new ArrayList<>())
                .build();
    }
}

