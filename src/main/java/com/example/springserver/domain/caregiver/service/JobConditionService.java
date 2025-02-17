package com.example.springserver.domain.caregiver.service;


import com.example.springserver.domain.caregiver.converter.JobConditionConverter;
import com.example.springserver.domain.caregiver.dto.request.JobConditionRequestDto.JobConditionReqDto;
import com.example.springserver.domain.caregiver.dto.response.JobConditionResponseDto;
import com.example.springserver.domain.caregiver.dto.response.JobConditionResponseDto.DetailJobConditionResponseDTO;
import com.example.springserver.domain.caregiver.dto.response.JobConditionResponseDto.JobConditionResponseDTO;
import com.example.springserver.domain.caregiver.entity.Caregiver;
import com.example.springserver.domain.caregiver.entity.JobCondition;
import com.example.springserver.domain.caregiver.entity.WorkLocation;
import com.example.springserver.domain.caregiver.repository.JobConditionRepository;
import com.example.springserver.domain.caregiver.repository.WorkLocationRepository;
import com.example.springserver.domain.location.entity.Location;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.GlobalException;
import com.example.springserver.global.security.util.CustomUserDetails;
import com.example.springserver.global.utils.FormatUtils;
import com.example.springserver.service.location.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobConditionService {

    private final CommonService commonService;
    private final LocationService locationService;
    private final JobConditionRepository jobConditionRepository;
    private final WorkLocationRepository workLocationRepository;

    @Transactional
    public JobConditionResponseDTO createOrUpdateJobCondition(CustomUserDetails user, JobConditionReqDto request) {
        Caregiver caregiver = commonService.getById(user);

        return jobConditionRepository.findByCaregiver(caregiver)
                .map(existingJobCondition -> updateJobCondition(caregiver, request)) // 존재하면 업데이트
                .orElseGet(() -> createJobCondition(caregiver, request)); // 없으면 새로 생성
    }

    @Transactional
    public JobConditionResponseDTO createJobCondition(Caregiver user, JobConditionReqDto request) {

        JobCondition jobCondition = JobCondition.builder()
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

        jobCondition = jobConditionRepository.save(jobCondition);

        saveLocations(request, jobCondition);

        return toJobConditionResponseDto(jobCondition);
    }


    @Transactional
    public JobConditionResponseDTO updateJobCondition(Caregiver user, JobConditionReqDto request) {

        JobCondition jobCondition = getJobCondition(user);

        workLocationRepository.deleteByJobCondition(jobCondition);
        workLocationRepository.flush();

        jobCondition.getWorkLocations().clear();

        jobCondition.updateInfo(request);

        jobCondition = jobConditionRepository.save(jobCondition);

        saveLocations(request, jobCondition);

        jobCondition = jobConditionRepository.findById(jobCondition.getId())
                .orElseThrow(() -> new GlobalException(ErrorCode.JOB_CONDITION_NOT_FOUND));

        return toJobConditionResponseDto(jobCondition);
    }

    @Transactional
    public void saveLocations(JobConditionReqDto request, JobCondition jobCondition) {
        final JobCondition finalJobCondition = jobCondition;

        List<WorkLocation> workLocations = request.getLocationRequestDTOList().stream()
                .map(dto -> {
                    Location location = locationService.findById(dto.getLocationId());
                    return WorkLocation.builder()
                            .jobCondition(finalJobCondition)
                            .locationId(location)
                            .build();
                })
                .collect(Collectors.toList());

        workLocationRepository.saveAll(workLocations);
        jobCondition.setWorkLocations(workLocations);
    }

    public DetailJobConditionResponseDTO getDetailedJobCondition(CustomUserDetails user) {
        Caregiver byId = commonService.getById(user);
        JobCondition jobCondition = getJobCondition(byId);
        return toDetailJobConditionResponseDto(byId,jobCondition);
    }

    public JobConditionResponseDTO getJobCondition(CustomUserDetails user) {
        Caregiver byId = commonService.getById(user);
        JobCondition jobCondition = getJobCondition(byId);
        return toJobConditionResponseDto(jobCondition);
    }

    private JobCondition getJobCondition(Caregiver user) {
        return jobConditionRepository.findByCaregiver(user)
                .orElseThrow(() -> new GlobalException(ErrorCode.JOB_CONDITION_NOT_FOUND));
    }

    private JobConditionResponseDTO toJobConditionResponseDto(JobCondition saved) {
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
                .locationRequestDtoList(saved.getWorkLocations().stream()
                        .map(dto -> JobConditionResponseDto.LocationResponseDTO.builder()
                                .workLocationId(dto.getId())
                                .locationName(locationService.getLocation(dto.getLocationId().getLocationId()))
                                .build()
                        )
                        .toList())
                .build();
    }

    private DetailJobConditionResponseDTO toDetailJobConditionResponseDto(Caregiver caregiver , JobCondition saved) {
        return DetailJobConditionResponseDTO.builder()
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
                                .locationName(locationService.getLocation(dto.getLocationId().getLocationId()))
                                .build()
                        )
                        .toList())
                .build();
    }

}
