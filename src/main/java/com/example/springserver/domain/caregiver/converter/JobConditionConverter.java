package com.example.springserver.domain.caregiver.converter;

import com.example.springserver.domain.caregiver.dto.response.JobConditionResponseDto;
import com.example.springserver.domain.caregiver.dto.response.JobConditionResponseDto.CertificateResponseDTO;
import com.example.springserver.domain.caregiver.dto.response.JobConditionResponseDto.ExperienceResponseDTO;
import com.example.springserver.domain.caregiver.dto.response.JobConditionResponseDto.JobConditionResponseDTO;
import com.example.springserver.domain.caregiver.entity.Certificate;
import com.example.springserver.domain.caregiver.entity.Experience;
import com.example.springserver.domain.caregiver.entity.JobCondition;
import com.example.springserver.domain.caregiver.entity.WorkLocation;
import com.example.springserver.service.location.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
                .locationRequestDtoList(toListResponseDto(saved.getWorkLocations()))
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
                .locationName(locationService.getLocation(location.getLocationId().getLocationId()))
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
}

