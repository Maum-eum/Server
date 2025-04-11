package com.example.springserver.domain.caregiver.service;

import com.example.springserver.domain.caregiver.converter.JobConditionCacheConverter;
import com.example.springserver.domain.caregiver.converter.JobConditionConverter;
import com.example.springserver.domain.caregiver.dto.request.JobConditionRequestDto;
import com.example.springserver.domain.caregiver.dto.request.JobConditionRequestDto.JobConditionReqDto;
import com.example.springserver.domain.caregiver.dto.response.JobConditionResponseDto.DetailJobConditionResponseDTO;
import com.example.springserver.domain.caregiver.dto.response.JobConditionResponseDto.JobConditionResponseDTO;
import com.example.springserver.domain.caregiver.entity.Caregiver;
import com.example.springserver.domain.caregiver.entity.JobCondition;
import com.example.springserver.domain.caregiver.entity.WorkLocation;
import com.example.springserver.domain.caregiver.repository.JobConditionCacheRepository;
import com.example.springserver.domain.caregiver.repository.JobConditionRepository;
import com.example.springserver.domain.caregiver.repository.WorkLocationRepository;
import com.example.springserver.domain.location.entity.Location;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.GlobalException;
import com.example.springserver.global.cache.model.JobConditionCache;
import com.example.springserver.global.security.util.CustomUserDetails;
import com.example.springserver.global.utils.FormatUtils;
import com.example.springserver.service.event.JobConditionChangedEvent;
import com.example.springserver.service.location.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobConditionService {

    private final CommonService commonService;
    private final LocationService locationService;
    private final JobConditionCacheRepository jobConditionCacheRepository;
    private final JobConditionRepository jobConditionRepository;
    private final WorkLocationRepository workLocationRepository;
    private final ApplicationEventPublisher eventPublisher;

    /*
    JC 변경 메서드입니다.
    여기서 eventPublisher 통해 JobcondChangedEvent 호출됩니다.
    이제 EventListener 가시면됩니다. -> ScoreRecalculateEventListener
     */
    @Transactional
    public JobConditionResponseDTO createOrUpdateJobCondition(CustomUserDetails user, JobConditionReqDto request) {
        Caregiver caregiver = commonService.getById(user);
        Long caregiverId = user.getId();

        // 캐시 데이터 삭제
        jobConditionCacheRepository.deleteByCaregiverId(caregiverId);

        JobConditionResponseDTO jobConditionResponseDTO = jobConditionRepository.findByCaregiver(caregiver)
                .map(existingJobCondition -> updateJobCondition(caregiver, request)) // 존재하면 업데이트
                .orElseGet(() -> createJobCondition(caregiver, request));// 없으면 새로 생성
        eventPublisher.publishEvent(new JobConditionChangedEvent(this,jobConditionResponseDTO.getJobConditionId()));

        // 새로운 데이터 캐싱
        JobCondition updatedJobCondition = findJobCondition(caregiver); // 방금 업데이트된 거니까 다시 조회
        JobConditionCache newCache = JobConditionCacheConverter.toRedisDto(updatedJobCondition);
        jobConditionCacheRepository.save(newCache);

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

    // 기존 jobCondition 조회 코드
//    public JobConditionResponseDTO getJobCondition(CustomUserDetails user) {
//        Caregiver byId = commonService.getById(user);
//        JobCondition jobCondition = findJobCondition(byId);
//        return JobConditionConverter.tojobConditionResponseDTO(jobCondition);
//    }

    // read-through 캐싱 전략이 적용된 조회 코드
    public JobConditionResponseDTO getJobCondition(CustomUserDetails user) {

        // Redis 캐시 먼저 조회
        Caregiver caregiver = commonService.getById(user);
        Optional<JobConditionCache> cachedJc = jobConditionCacheRepository.findByCaregiverId(user.getId());

        if (cachedJc.isPresent()) {
            log.info("[Redis] jobConditionCache 조회 ======== ");
            return JobConditionCacheConverter.fromRedisDto(cachedJc.get()); // 캐싱 데이터 return
        }

        // DB 조회
        log.info("[MySQL] jobCondition 조회 ======== ");
        JobCondition jobCondition = findJobCondition(caregiver);

        // Redis 캐시 저장
        JobConditionCache cache = JobConditionCacheConverter.toRedisDto(jobCondition);
        jobConditionCacheRepository.save(cache);

        return JobConditionConverter.tojobConditionResponseDTO(jobCondition); // DB 데이터 캐싱 후 return
    }

    public JobCondition findJobCondition(Caregiver user) {
        return jobConditionRepository.findByCaregiver(user)
                .orElseThrow(() -> new GlobalException(ErrorCode.JOB_CONDITION_NOT_FOUND));
    }
}
