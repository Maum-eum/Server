package com.example.springserver.domain.caregiver.converter;

import com.example.springserver.domain.caregiver.dto.JobConditionOptionInfo;
import com.example.springserver.domain.caregiver.dto.request.JobConditionRequestDto;
import com.example.springserver.domain.caregiver.dto.response.JobConditionResponseDto;
import com.example.springserver.domain.caregiver.dto.response.JobConditionResponseDto.CertificateResponse;
import com.example.springserver.domain.caregiver.dto.response.JobConditionResponseDto.ExperienceResponse;
import com.example.springserver.domain.caregiver.dto.response.JobConditionResponseDto.Response;
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

    public static Response toJobConditionResponseDTO(JobCondition saved){
        return Response.builder()
                .jobConditionId(saved.getId())
                .jobConditionOptionInfo(
                        JobConditionOptionInfo.builder()
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
                                .build()
                )
                .locationResponseList(toListResponseDto(saved.getWorkLocations()))
                .caregiverId(saved.getCaregiver().getId())
                .build();
    }

    public static JobConditionResponseDto.DetailResponse toDetailJobConditionResponseDto(Caregiver caregiver, JobCondition saved) {
        return JobConditionResponseDto.DetailResponse.builder()
                .name(caregiver.getName())
                .contact(caregiver.getContact())
                .car(caregiver.getCar())
                .education(caregiver.getEducation())
                .intro(caregiver.getIntro())
                .address(caregiver.getAddress())
                .employmentStatus(caregiver.getEmploymentStatus())
                .certificateResponseList(caregiver.getCertificates().stream()
                        .map(JobConditionConverter::toResponseCertificate)
                        .toList())
                .experienceResponseList(caregiver.getExperiences().stream()
                        .map(JobConditionConverter::toResponseExperience)
                        .toList())
                .img(caregiver.getImg())
                .jobConditionId(saved.getId())
                .jobConditionBaseDto(
                        JobConditionOptionInfo.builder()
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
                                .build()
                )
                .locationResponseList(saved.getWorkLocations().stream()
                        .map(dto -> JobConditionResponseDto.LocationResponse.builder()
                                .workLocationId(dto.getId())
                                .locationName(locationService.getLocation(dto.getLocation().getLocationId()))
                                .build()
                        )
                        .toList())
                .build();
    }

    private static List<JobConditionResponseDto.LocationResponse> toListResponseDto(List<WorkLocation> workLocations) {
        return workLocations.stream()
                .map(JobConditionConverter::toWorkLocationResponseDto)
                .collect(Collectors.toList());
    }

    private static JobConditionResponseDto.LocationResponse toWorkLocationResponseDto(WorkLocation location) {

        return JobConditionResponseDto.LocationResponse.builder()
                .workLocationId(location.getId())
                .locationName(locationService.getLocation(location.getLocation().getLocationId()))
                .build();
    }

    public static CertificateResponse toResponseCertificate(Certificate certificate) {
        return CertificateResponse.builder()
                .certNum(certificate.getCertNum())
                .certRate(certificate.getCertRate())
                .certType(certificate.getCertType())
                .build();
    }

    public static ExperienceResponse toResponseExperience(Experience experience) {
        return ExperienceResponse.builder()
                .duration(experience.getDuration())
                .title(experience.getTitle())
                .description(experience.getDescription())
                .build();
    }

    public static JobCondition from(Caregiver user, JobConditionRequestDto.Request request) {
        return JobCondition.builder()
                .caregiver(user)
                .bathingAssist(request.getJobConditionOptionInfo().getBathingAssist())
                .catheterOrStomaCare(request.getJobConditionOptionInfo().getCatheterOrStomaCare())
                .diaperCare(request.getJobConditionOptionInfo().getDiaperCare())
                .cleaningLaundryAssist(request.getJobConditionOptionInfo().getCleaningLaundryAssist())
                .selfToileting(request.getJobConditionOptionInfo().getSelfToileting())
                .selfFeeding(request.getJobConditionOptionInfo().getSelfFeeding())
                .cognitiveStimulation(request.getJobConditionOptionInfo().getCognitiveStimulation())
                .cookingAssistance(request.getJobConditionOptionInfo().getCookingAssistance())
                .desiredHourlyWage(request.getJobConditionOptionInfo().getDesiredHourlyWage())
                .emotionalSupport(request.getJobConditionOptionInfo().getEmotionalSupport())
                .enteralNutritionSupport(request.getJobConditionOptionInfo().getEnteralNutritionSupport())
                .exerciseSupport(request.getJobConditionOptionInfo().getExerciseSupport())
                .hospitalAccompaniment(request.getJobConditionOptionInfo().getHospitalAccompaniment())
                .flexibleSchedule(request.getJobConditionOptionInfo().getFlexibleSchedule())
                .mealPreparation(request.getJobConditionOptionInfo().getMealPreparation())
                .immobile(request.getJobConditionOptionInfo().getImmobile())
                .occasionalToiletingAssist(request.getJobConditionOptionInfo().getOccasionalToiletingAssist())
                .mobilityAssist(request.getJobConditionOptionInfo().getMobilityAssist())
                .wheelchairAssist(request.getJobConditionOptionInfo().getWheelchairAssist())
                .independentMobility(request.getJobConditionOptionInfo().getIndependentMobility())
                .dayOfWeek(FormatUtils.toIntegerDayOfWeek(request.getJobConditionOptionInfo().getDayOfWeek()))
                .startTime(request.getJobConditionOptionInfo().getStartTime())
                .endTime(request.getJobConditionOptionInfo().getEndTime())
                .workLocations(new ArrayList<>())
                .build();
    }
}

