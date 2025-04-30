package com.example.springserver.domain.caregiver.service;

import com.example.springserver.domain.caregiver.cache.JobConditionCache;
import com.example.springserver.domain.caregiver.cache.JobConditionCacheConverter;
import com.example.springserver.domain.caregiver.converter.JobConditionConverter;
import com.example.springserver.domain.caregiver.dto.request.JobConditionRequestDto;
import com.example.springserver.domain.caregiver.dto.request.JobConditionRequestDto.Request;
import com.example.springserver.domain.caregiver.dto.response.JobConditionResponseDto.DetailResponse;
import com.example.springserver.domain.caregiver.dto.response.JobConditionResponseDto.Response;
import com.example.springserver.domain.caregiver.entity.Caregiver;
import com.example.springserver.domain.caregiver.entity.JobCondition;
import com.example.springserver.domain.caregiver.entity.WorkLocation;
import com.example.springserver.domain.caregiver.repository.CaregiverRepository;
import com.example.springserver.domain.caregiver.repository.JobConditionRepository;
import com.example.springserver.domain.caregiver.repository.WorkLocationRepository;
import com.example.springserver.domain.caregiver.service.cache.JobConditionCacheService;
import com.example.springserver.domain.location.entity.Location;
import com.example.springserver.global.apiPayload.format.CacheException;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.GlobalException;
import com.example.springserver.global.security.util.CustomUserDetails;
import com.example.springserver.service.event.JobConditionChangedEvent;
import com.example.springserver.service.location.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobConditionService {
    private final LocationService locationService;
    private final CaregiverRepository caregiverRepository;
    private final JobConditionRepository jobConditionRepository;
    private final WorkLocationRepository workLocationRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final JobConditionCacheService jobConditionCacheService;

    /*
    JC 변경 메서드입니다.
    여기서 eventPublisher 통해 JobcondChangedEvent 호출됩니다.
    이제 EventListener 가시면됩니다. -> ScoreRecalculateEventListener
     */
    @Transactional
    public Response createJobCondition(CustomUserDetails user, Request request) {
        Caregiver caregiver = getValidCaregiver(user.getId());
        // 캐시 삭제
        jobConditionCacheService.deleteByCaregiverKey(caregiver.getId());

        // 생성
        JobCondition jobCondition = JobConditionConverter.from(caregiver, request);
        jobCondition = jobConditionRepository.save(jobCondition);

        saveLocations(request, jobCondition);

        return postProcess(jobCondition);
    }

    @Transactional
    public Response updateJobCondition(CustomUserDetails userDetails, Request request) {
        Caregiver caregiver = getValidCaregiver(userDetails.getId());

        jobConditionCacheService.deleteByCaregiverKey(caregiver.getId());

        JobCondition jobCondition = getValidJobCondition(caregiver);
        jobCondition.updateInfo(request);
        updateWorkLocations(jobCondition, request);
        jobCondition = jobConditionRepository.save(jobCondition);

        return postProcess(jobCondition);
    }

    public void saveLocations(Request request, JobCondition jobCondition) {

        List<WorkLocation> workLocations = request.getLocationRequestList().stream()
                .map(dto -> {
                    Location location = locationService.findById(dto.getLocationId());
                    return WorkLocation.builder()
                            .jobCondition(jobCondition)
                            .location(location)
                            .build();
                })
                .toList();

        workLocationRepository.saveAll(workLocations);
        jobCondition.setWorkLocations(workLocations);
    }

    private void updateWorkLocations(JobCondition jobCondition, Request request) {
        List<Long> locationIds = request.getLocationRequestList().stream()
                .map(JobConditionRequestDto.LocationRequest::getLocationId)
                .toList();

        Set<Location> requestedLocations = new HashSet<>(locationService.findAllById(locationIds));
        List<WorkLocation> currentLocations = jobCondition.getWorkLocations();

        // 기존에 없는 새로운 Location만 추가
        for (Location location : requestedLocations) {
            boolean alreadyExists = currentLocations.stream()
                    .anyMatch(wl -> wl.getLocation().getLocationId().equals(location.getLocationId())); // 수정 포인트

            if (!alreadyExists) {
                WorkLocation workLocation = WorkLocation.builder()
                        .jobCondition(jobCondition)
                        .location(location)
                        .build();
                workLocationRepository.save(workLocation);
                jobCondition.addWokLocation(workLocation);  // 양방향 연관관계 설정
            }
        }
    }

    public DetailResponse getDetailedJobCondition(CustomUserDetails user) {
        Caregiver byId = getValidCaregiver(user.getId());
        JobCondition jobCondition = getValidJobCondition(byId);

        return JobConditionConverter.toDetailJobConditionResponseDto(byId,jobCondition);
    }

    // Read Through 캐싱 (Redis + Local Cache)
    @Transactional(readOnly = true)
    public Response getJobCondition(CustomUserDetails user) {
        JobConditionCache cachedJc = null;

        try {
            // 로컬 or Redis 캐시 조회
            cachedJc = jobConditionCacheService.getByCaregiverKey(user.getId());

            if (cachedJc != null) {
                log.info("[CACHE HIT] jobCondition 조회 ======== ");
                return JobConditionCacheConverter.fromRedisDto(cachedJc);
            }
        } catch (CacheException ce) {
            log.warn("[CACHE MISS] jobCondition 캐시 조회 불가 ======== ");
        }

        // DB 조회
        log.info("[MySQL] jobCondition 조회 ======== ");
        Caregiver caregiver = getValidCaregiver(user.getId());
        JobCondition jobCondition = getValidJobCondition(caregiver);

        // DB -> 캐시 저장
        JobConditionCache toCache = JobConditionCacheConverter.toRedisDto(jobCondition);
        jobConditionCacheService.save(toCache);

        return JobConditionConverter.toJobConditionResponseDTO(jobCondition);
    }

    public Caregiver getValidCaregiver(Long caregiverId) {
        return caregiverRepository.findById(caregiverId)
                .orElseThrow(() -> new GlobalException(ErrorCode.CAREGIVER_IS_NOT_EXIST));
    }

    public JobCondition getValidJobCondition(Caregiver user) {
        return jobConditionRepository.findByCaregiver(user)
                .orElseThrow(() -> new GlobalException(ErrorCode.JOB_CONDITION_NOT_FOUND));
    }

    private Response postProcess(JobCondition jobCondition) {
        Response jcDto = JobConditionConverter.toJobConditionResponseDTO(jobCondition);

        // 이벤트 발행
        if (jobCondition.getId() != null) {
            eventPublisher.publishEvent(new JobConditionChangedEvent(this, jobCondition.getId()));
        }

        // 캐시 저장
        JobConditionCache cacheData = JobConditionCacheConverter.toRedisDto(jobCondition);
        jobConditionCacheService.save(cacheData);

        return jcDto;
    }
}