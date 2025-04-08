package com.example.springserver.domain.caregiver.service;

import com.example.springserver.domain.caregiver.converter.JobConditionConverter;
import com.example.springserver.domain.caregiver.dto.request.JobConditionRequestDto;
import com.example.springserver.domain.caregiver.dto.request.JobConditionRequestDto.JobConditionReqDto;
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
import com.example.springserver.global.cache.model.CacheJobCondition;
import com.example.springserver.global.security.util.CustomUserDetails;
import com.example.springserver.global.utils.FormatUtils;
import com.example.springserver.service.event.JobConditionChangedEvent;
import com.example.springserver.service.location.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobConditionService {

    private final CommonService commonService;
    private final LocationService locationService;
    private final JobConditionRepository jobConditionRepository;
    private final WorkLocationRepository workLocationRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final RedisTemplate<String, CacheJobCondition> redisTemplate;

    /*
    JC 변경 메서드입니다.
    여기서 eventPublisher 통해 JobcondChangedEvent 호출됩니다.
    이제 EventListener 가시면됩니다. -> ScoreRecalculateEventListener
     */
    @Transactional
    public JobConditionResponseDTO createOrUpdateJobCondition(CustomUserDetails user, JobConditionReqDto request) {
        Caregiver caregiver = commonService.getById(user);
        JobConditionResponseDTO jobConditionResponseDTO = jobConditionRepository.findByCaregiver(caregiver)
                .map(existingJobCondition -> updateJobCondition(caregiver, request)) // 존재하면 업데이트
                .orElseGet(() -> createJobCondition(caregiver, request));// 없으면 새로 생성
        eventPublisher.publishEvent(new JobConditionChangedEvent(this,jobConditionResponseDTO.getJobConditionId()));
        return jobConditionResponseDTO;
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

        return JobConditionConverter.tojobConditionResponseDTO(jobCondition);
    }


    @Transactional
    public JobConditionResponseDTO updateJobCondition(Caregiver user, JobConditionReqDto request) {
        JobCondition jobCondition = findJobCondition(user);

        Set<Long> updatedIds = new HashSet<>();

        for (JobConditionRequestDto.LocationRequestDTO dto : request.getLocationRequestDTOList()) {
            Location location = locationService.findById(dto.getLocationId());

            if (dto.getWorkLocationId() != null) {
                WorkLocation existing = jobCondition.getWorkLocations().stream()
                        .filter(wl -> wl.getId().equals(dto.getWorkLocationId()))
                        .findFirst()
                        .orElseThrow(() -> new GlobalException(ErrorCode.WORK_LOCATION_NOT_FOUND));

                existing.setLocationId(location);
                updatedIds.add(existing.getId());
            }
            else {
                WorkLocation newLocation = WorkLocation.builder()
                        .locationId(location)
                        .jobCondition(jobCondition)
                        .build();
                jobCondition.addWokLocation(newLocation);
            }
        }

        jobCondition.getWorkLocations().removeIf(wl -> !updatedIds.contains(wl.getId()));

        jobCondition.updateInfo(request);
        return JobConditionConverter.tojobConditionResponseDTO(jobCondition);
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
        JobCondition jobCondition = findJobCondition(byId);
        return JobConditionConverter.toDetailJobConditionResponseDto(byId,jobCondition);
    }

    public JobConditionResponseDTO getJobCondition(CustomUserDetails user) {
        Caregiver byId = commonService.getById(user);
        JobCondition jobCondition = findJobCondition(byId);
        return JobConditionConverter.tojobConditionResponseDTO(jobCondition);
    }

    public JobCondition findJobCondition(Caregiver user) {
        return jobConditionRepository.findByCaregiver(user)
                .orElseThrow(() -> new GlobalException(ErrorCode.JOB_CONDITION_NOT_FOUND));
    }

    private String getRedisKey(Long caregiverId) {
        return "jobCondition:" + caregiverId;
    }

}
