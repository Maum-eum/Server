package com.example.springserver.domain.caregiver.service;

import com.example.springserver.domain.caregiver.cache.JobConditionCache;
import com.example.springserver.domain.caregiver.cache.JobConditionCacheConverter;
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

    private final CommonService commonService;
    private final LocationService locationService;
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
    public JobConditionResponseDTO createJobCondition(CustomUserDetails user, JobConditionReqDto request) {
        Caregiver caregiver = commonService.getById(user);
        // 캐시 삭제
        jobConditionCacheService.deleteByCaregiverKey(caregiver.getId());

        // 생성
        JobCondition jobCondition = JobConditionConverter.from(caregiver, request);
        jobCondition = jobConditionRepository.save(jobCondition);

        saveLocations(request, jobCondition);

        return postProcess(jobCondition);
    }

    @Transactional
    public JobConditionResponseDTO updateJobCondition(CustomUserDetails userDetails, JobConditionReqDto request) {
        Caregiver caregiver = commonService.getById(userDetails);
        Long caregiverId = caregiver.getId();

        jobConditionCacheService.deleteByCaregiverKey(caregiverId);

        JobCondition jobCondition = jobConditionRepository.findByCaregiver(caregiver)
                .orElseThrow(() -> new GlobalException(ErrorCode.JOB_CONDITION_NOT_FOUND));

        jobCondition.updateInfo(request);
        updateWorkLocations(jobCondition, request);
        jobCondition = jobConditionRepository.save(jobCondition);

        return postProcess(jobCondition);
    }

    public void saveLocations(JobConditionReqDto request, JobCondition jobCondition) {

        List<WorkLocation> workLocations = request.getLocationRequestDTOList().stream()
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

    private void updateWorkLocations(JobCondition jobCondition, JobConditionReqDto request) {
        List<Long> locationIds = request.getLocationRequestDTOList().stream()
                .map(JobConditionRequestDto.LocationRequestDTO::getLocationId)
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

    public DetailJobConditionResponseDTO getDetailedJobCondition(CustomUserDetails user) {
        Caregiver byId = commonService.getById(user);
        JobCondition jobCondition = findJobCondition(byId);
        return JobConditionConverter.toDetailJobConditionResponseDto(byId,jobCondition);
    }

    // read-through 캐싱 전략이 적용된 조회 코드
    @Transactional(readOnly = true)
    public JobConditionResponseDTO getJobCondition(CustomUserDetails user) {
        try {
            JobConditionCache cachedJc = jobConditionCacheService.getByCaregiverKey(user.getId());

            // Cache Hit : Redis 조회
            log.info("[Redis] jobCondition 조회 ======== ");
            return JobConditionCacheConverter.fromRedisDto(cachedJc);
        } catch (CacheException ce) {
            // Cache Miss : DB 직접 조회
            log.info("[MySQL] jobCondition 조회 ======== ");

            Caregiver caregiver = commonService.getById(user);
            JobCondition jobCondition = findJobCondition(caregiver);

            // DB 조회 후 Caching
            jobConditionCacheService.save(JobConditionCacheConverter.toRedisDto(jobCondition));
            return JobConditionConverter.tojobConditionResponseDTO(jobCondition);
        }
    }

    public JobCondition findJobCondition(Caregiver user) {
        return jobConditionRepository.findByCaregiver(user)
                .orElseThrow(() -> new GlobalException(ErrorCode.JOB_CONDITION_NOT_FOUND));
    }

    private JobConditionResponseDTO postProcess(JobCondition jobCondition) {
        JobConditionResponseDTO jcDto = JobConditionConverter.tojobConditionResponseDTO(jobCondition);

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
