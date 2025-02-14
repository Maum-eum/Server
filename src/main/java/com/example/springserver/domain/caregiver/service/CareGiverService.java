package com.example.springserver.domain.caregiver.service;

import com.example.springserver.domain.caregiver.converter.CaregiverConverter;
import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDTO;
import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDTO.JobConditionRequestDTO;
import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDTO.LocationRequestDTO;
import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDTO.UpdateCaregiverReq;
import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDTO.WorkTimeRequestDTO;
import com.example.springserver.domain.caregiver.dto.response.CaregiverResponseDTO;
import com.example.springserver.domain.caregiver.dto.response.CaregiverResponseDTO.JobConditionResponseDTO;
import com.example.springserver.domain.caregiver.entity.Caregiver;
import com.example.springserver.domain.caregiver.entity.JobCondition;
import com.example.springserver.domain.caregiver.entity.WorkLocation;
import com.example.springserver.domain.caregiver.entity.WorkTime;
import com.example.springserver.domain.caregiver.repository.CaregiverRepository;
import com.example.springserver.domain.caregiver.repository.JobConditionRepository;
import com.example.springserver.domain.caregiver.repository.WorkLocationRepository;
import com.example.springserver.domain.caregiver.repository.WorkTimeRepository;
import com.example.springserver.domain.center.converter.TimeConverter;
import com.example.springserver.domain.location.entity.Location;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.GlobalException;
import com.example.springserver.global.security.util.CustomUserDetails;
import com.example.springserver.repository.location.LocationRepository;
import com.example.springserver.service.location.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CareGiverService {

    private final CaregiverRepository caregiverRepository;
    private final JobConditionRepository jobConditionRepository;
    private final WorkTimeRepository workTimeRepository;
    private final WorkLocationRepository workLocationRepository;
    private final LocationRepository locationRepository;
    private final LocationService locationService;

    public Caregiver getUserInfo(CustomUserDetails user) {
        Long userId = user.getId();
        return caregiverRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public Caregiver updateUserInfo(CustomUserDetails user, UpdateCaregiverReq request) {
        Caregiver caregiver = getById(user);
        caregiver.setUpdate(caregiver,request);
        return caregiverRepository.save(caregiver);
    }

    @Transactional
    public JobConditionResponseDTO createJobCondition(CustomUserDetails user, JobConditionRequestDTO request) {
        Caregiver caregiver = getById(user);

        JobCondition jobCondition = JobCondition.builder()
                .caregiver(caregiver)
                .bathingAssist(defaultFalse(request.getBathingAssist()))
                .catheterOrStomaCare(defaultFalse(request.getCatheterOrStomaCare()))
                .diaperCare(defaultFalse(request.getDiaperCare()))
                .cleaningLaundryAssist(defaultFalse(request.getCleaningLaundryAssist()))
                .selfToileting(defaultFalse(request.getSelfToileting()))
                .selfFeeding(defaultFalse(request.getSelfFeeding()))
                .cognitiveStimulation(defaultFalse(request.getCognitiveStimulation()))
                .cookingAssistance(defaultFalse(request.getCookingAssistance()))
                .desiredHourlyWage(defaultFalse(request.getDesiredHourlyWage()))
                .emotionalSupport(defaultFalse(request.getEmotionalSupport()))
                .enteralNutritionSupport(defaultFalse(request.getEnteralNutritionSupport()))
                .exerciseSupport(defaultFalse(request.getExerciseSupport()))
                .hospitalAccompaniment(defaultFalse(request.getHospitalAccompaniment()))
                .flexibleSchedule(defaultFalse(request.getFlexibleSchedule()))
                .mealPreparation(defaultFalse(request.getMealPreparation()))
                .immobile(defaultFalse(request.getImmobile()))
                .occasionalToiletingAssist(defaultFalse(request.getOccasionalToiletingAssist()))
                .mobilityAssist(defaultFalse(request.getMobilityAssist()))
                .wheelchairAssist(defaultFalse(request.getWheelchairAssist()))
                .independentMobility(defaultFalse(request.getIndependentMobility()))
                .workTimes(new ArrayList<>())
                .workLocations(new ArrayList<>())
                .build();

        jobCondition = jobConditionRepository.save(jobCondition);

        saveWorkTimesAndLocations(request, jobCondition);

        return toJobConditionResponseDto(jobCondition);
    }


    @Transactional
    public JobConditionResponseDTO updateJobCondition(CustomUserDetails user, JobConditionRequestDTO request) {
        Caregiver caregiver = getById(user);

        JobCondition jobCondition = jobConditionRepository.findByCaregiver(caregiver)
                .orElseThrow(() -> new GlobalException(ErrorCode.JOB_CONDITION_NOT_FOUND));

        workTimeRepository.deleteByJobCondition(jobCondition);
        workLocationRepository.deleteByJobCondition(jobCondition);

        jobCondition.getWorkTimes().clear();
        jobCondition.getWorkLocations().clear();

        jobCondition.updateInfo(request);

        jobCondition = jobConditionRepository.save(jobCondition);

        saveWorkTimesAndLocations(request, jobCondition);

        jobCondition = jobConditionRepository.findById(jobCondition.getId())
                .orElseThrow(() -> new GlobalException(ErrorCode.JOB_CONDITION_NOT_FOUND));

        return toJobConditionResponseDto(jobCondition);
    }






    private void saveWorkTimesAndLocations(JobConditionRequestDTO request, JobCondition jobCondition) {
        final JobCondition finalJobCondition = jobCondition;

        List<WorkTime> workTimes = request.getWorkTimeRequestDTOList().stream()
                .map(dto -> WorkTime.builder()
                        .jobCondition(finalJobCondition)  // `managed` 상태의 jobCondition 사용
                        .startTime((long) dto.getStartTime())
                        .endTime((long) dto.getEndTime())
                        .dayOfWeek(dto.getDayOfWeek())
                        .build())
                .collect(Collectors.toList());

        workTimeRepository.saveAll(workTimes);
        jobCondition.setWorkTimes(workTimes);

        List<WorkLocation> workLocations = request.getLocationRequestDTOList().stream()
                .map(dto -> {
                    Location location = locationRepository.findById(dto.getLocationId())
                            .orElseThrow(() -> new GlobalException(ErrorCode.LOCATION_NOT_FOUND));
                    return WorkLocation.builder()
                            .jobCondition(finalJobCondition)
                            .locationId(location)
                            .build();
                })
                .collect(Collectors.toList());

        workLocationRepository.saveAll(workLocations);
        jobCondition.setWorkLocations(workLocations);
    }




    private Boolean defaultFalse(Boolean value) {
        return value != null ? value : false;
    }

    private Caregiver getById(CustomUserDetails user) throws GlobalException {
        return caregiverRepository.findById(user.getId()).orElseThrow(()-> new GlobalException(ErrorCode.USER_NOT_FOUND));
    }

    private JobConditionResponseDTO toJobConditionResponseDto(JobCondition saved) {
        return JobConditionResponseDTO.builder()
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
                .locationRequestDTOList(saved.getWorkLocations().stream()
                        .map(dto -> CaregiverResponseDTO.LocationResponseDTO.builder()
                                .workLocationId(dto.getId())
                                .locationName(locationService.getLocation(dto.getLocationId().getLocationId()))
                                .build()
                        )
                        .toList())
                .workTimeRequestDTOList(saved.getWorkTimes().stream()
                        .map(dto -> CaregiverResponseDTO.WorkTimeResponseDTO.builder()
                                .workTimeId(dto.getId())
                                .start_time(TimeConverter.convertToDateTime(dto.getStartTime()))
                                .end_time(TimeConverter.convertToDateTime(dto.getEndTime()))
                                .dayOfWeek(dto.getDayOfWeek())
                                .build())
                        .toList())
                .build();
    }

    public JobConditionResponseDTO getJobCondition(CustomUserDetails user) {
        Caregiver byId = getById(user);
        JobCondition jobCondition = jobConditionRepository.findByCaregiver(byId)
                .orElseThrow(() -> new GlobalException(ErrorCode.JOB_CONDITION_NOT_FOUND));
        return toJobConditionResponseDto(jobCondition);
    }
}
