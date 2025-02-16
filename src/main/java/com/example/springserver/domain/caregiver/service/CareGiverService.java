package com.example.springserver.domain.caregiver.service;

import com.example.springserver.domain.caregiver.converter.CaregiverConverter;
import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDTO.*;
import com.example.springserver.domain.caregiver.dto.response.CaregiverResponseDTO;
import com.example.springserver.domain.caregiver.dto.response.CaregiverResponseDTO.DetailJobConditionResponseDTO;
import com.example.springserver.domain.caregiver.dto.response.CaregiverResponseDTO.JobConditionResponseDTO;
import com.example.springserver.domain.caregiver.entity.Caregiver;
import com.example.springserver.domain.caregiver.entity.JobCondition;
import com.example.springserver.domain.caregiver.entity.WorkLocation;
import com.example.springserver.domain.caregiver.repository.CaregiverRepository;
import com.example.springserver.domain.caregiver.repository.JobConditionRepository;
import com.example.springserver.domain.caregiver.repository.WorkLocationRepository;
import com.example.springserver.domain.center.entity.Match;
import com.example.springserver.domain.center.entity.enums.MatchStatus;
import com.example.springserver.domain.center.entity.enums.RecruitStatus;
import com.example.springserver.domain.center.repository.MatchRepository;
import com.example.springserver.domain.location.entity.Location;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.GlobalException;
import com.example.springserver.global.security.util.CustomUserDetails;
import com.example.springserver.repository.location.LocationRepository;
import com.example.springserver.service.location.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CareGiverService {

    private final CaregiverRepository caregiverRepository;
    private final JobConditionRepository jobConditionRepository;
    private final WorkLocationRepository workLocationRepository;
    private final LocationRepository locationRepository;
    private final LocationService locationService;
    private final MatchRepository matchRepository;

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
    public JobConditionResponseDTO createOrUpdateJobCondition(CustomUserDetails user, JobConditionRequestDTO request) {
        Caregiver caregiver = getById(user);

        return jobConditionRepository.findByCaregiver(caregiver)
                .map(existingJobCondition -> updateJobCondition(caregiver, request)) // 존재하면 업데이트
                .orElseGet(() -> createJobCondition(caregiver, request)); // 없으면 새로 생성
    }

    @Transactional
    public JobConditionResponseDTO createJobCondition(Caregiver user, JobConditionRequestDTO request) {

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
                .dayOfWeek(toIntegerDayOfWeek(request.getDayOfWeek()))
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .workLocations(new ArrayList<>())
                .build();

        jobCondition = jobConditionRepository.save(jobCondition);

        saveLocations(request, jobCondition);

        return toJobConditionResponseDto(jobCondition);
    }


    @Transactional
    public JobConditionResponseDTO updateJobCondition(Caregiver user, JobConditionRequestDTO request) {

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
    public Boolean changeStatus(CustomUserDetails user) {
        Caregiver byId = getById(user);
        byId.setEmploymentStatus(!byId.getEmploymentStatus());
        return caregiverRepository.save(byId).getEmploymentStatus();
    }

    public JobConditionResponseDTO getJobCondition(CustomUserDetails user) {
        Caregiver byId = getById(user);
        JobCondition jobCondition = getJobCondition(byId);
        return toJobConditionResponseDto(jobCondition);
    }

    public DetailJobConditionResponseDTO getDetailedJobCondition(CustomUserDetails user) {
        Caregiver byId = getById(user);
        JobCondition jobCondition = getJobCondition(byId);
        return toDetailJobConditionResponseDto(byId,jobCondition);
    }

    private void saveLocations(JobConditionRequestDTO request, JobCondition jobCondition) {
        final JobCondition finalJobCondition = jobCondition;

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
                        .map(CaregiverConverter::toResponseCertificate)
                        .toList())
                .experienceResponseDTOList(caregiver.getExperiences().stream()
                        .map(CaregiverConverter::toResponseExperience)
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
                .dayOfWeek(toStringDayOfWeek(saved.getDayOfWeek()))
                .startTime(saved.getStartTime())
                .endTime(saved.getEndTime())
                .locationRequestDTOList(saved.getWorkLocations().stream()
                        .map(dto -> CaregiverResponseDTO.LocationResponseDTO.builder()
                                .workLocationId(dto.getId())
                                .locationName(locationService.getLocation(dto.getLocationId().getLocationId()))
                                .build()
                        )
                        .toList())
                .build();
    }

    private Caregiver getById(CustomUserDetails user) throws GlobalException {
        return caregiverRepository.findById(user.getId()).orElseThrow(()-> new GlobalException(ErrorCode.USER_NOT_FOUND));
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
                .locationRequestDTOList(saved.getWorkLocations().stream()
                        .map(dto -> CaregiverResponseDTO.LocationResponseDTO.builder()
                                .workLocationId(dto.getId())
                                .locationName(locationService.getLocation(dto.getLocationId().getLocationId()))
                                .build()
                        )
                        .toList())
                .build();
    }



    private JobCondition getJobCondition(Caregiver user) {
        return jobConditionRepository.findByCaregiver(user)
                .orElseThrow(() -> new GlobalException(ErrorCode.JOB_CONDITION_NOT_FOUND));
    }

    @Transactional
    public String responseToRecruit(CustomUserDetails user, RecruitReq request) {
        Match originalMatch = matchRepository.findById(request.getMatchId())
                .orElseThrow(() -> new GlobalException(ErrorCode.MATCH_NOT_FOUND));

        Caregiver caregiver = getById(user);

        if (request.getStatus() == RecruitStatus.ACCEPTED || request.getStatus() == RecruitStatus.TUNING) {
            originalMatch.setStatus(MatchStatus.TUNING);
            caregiver.setEmploymentStatus(false);
        } else {
            originalMatch.setStatus(MatchStatus.DECLINED);
            originalMatch.setDeletedAt(LocalDateTime.now());
        }

        return "Status Updated";
    }

    public Integer toIntegerDayOfWeek(String binaryString) {
        if (!binaryString.matches("^[01]{7}$")) {
            throw new IllegalArgumentException("Invalid binary string. Must be exactly 7 digits of 0s and 1s.");
        }
        return Integer.parseInt(binaryString, 2);
    }

    public String toStringDayOfWeek(Integer week){
        return Integer.toBinaryString(week);
    }

}
