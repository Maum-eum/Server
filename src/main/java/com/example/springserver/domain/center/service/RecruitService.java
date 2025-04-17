package com.example.springserver.domain.center.service;

import com.example.springserver.domain.center.cache.RecruitConditionCache;
import com.example.springserver.domain.center.cache.RecruitConditionCacheConverter;
import com.example.springserver.domain.center.converter.RecruitConverter;
import com.example.springserver.domain.center.dto.request.RecruitRequestDto.RequestDto;
import com.example.springserver.domain.center.dto.request.RecruitRequestDto.RequestTimeDto;
import com.example.springserver.domain.center.dto.response.RecruitResponseDto;
import com.example.springserver.domain.center.dto.response.RecruitResponseDto.ResponseDto;
import com.example.springserver.domain.center.entity.Elder;
import com.example.springserver.domain.center.entity.RecruitCondition;
import com.example.springserver.domain.center.repository.ElderRepository;
import com.example.springserver.domain.center.repository.RecruitConditionRepository;
import com.example.springserver.domain.center.service.cache.RecruitConditionCacheService;
import com.example.springserver.domain.location.entity.Location;
import com.example.springserver.global.apiPayload.format.CacheException;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.GlobalException;
import com.example.springserver.global.apiPayload.format.RecruitException;
import com.example.springserver.global.validation.validator.RecruitLaborLawValidator;
import com.example.springserver.repository.location.LocationRepository;
import com.example.springserver.service.event.RecruitConditionChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecruitService {

    private final RecruitConditionRepository recruitConditionRepository;
    private final LocationRepository locationRepository;
    private final ElderRepository elderRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final RecruitLaborLawValidator recruitLaborLawValidator;
    private final RecruitConditionCacheService recruitConditionCacheService;

    public List<ResponseDto> getRecruitConditionList(Long centerId, Long elderId) {
        validateElderBelongsToCenter(elderId, centerId);

        try { // Cache Hit : Redis 조회
            log.info("[Redis] recruitCondition 리스트 조회 ========");

            List<RecruitConditionCache> cachedList = recruitConditionCacheService.getByElderIdFromRedis(elderId);
            return RecruitConditionCacheConverter.fromCacheList(cachedList);
        } catch(CacheException ce) { // Cache Miss : DB 직접 조회
            log.info("[MySQL] recruitCondition 리스트 조회 ========");

            List<RecruitCondition> conditions = recruitConditionRepository.findWithRecruitTimesByElderId(elderId);
            recruitConditionCacheService.saveAll(RecruitConditionCacheConverter.toCacheList(conditions));
            return RecruitConverter.toListResponseDto(conditions);
        }
    }

    public RecruitResponseDto.ResponseDto getRecruitCondition(Long centerId, Long elderId, Long recruitId) {
        validateElderBelongsToCenter(elderId, centerId);

        try { // Cache Hit : Redis 조회
            log.info("[Redis] recruitCondition 단건 조회 ========");

            RecruitConditionCache cachedRecruitCondition = getRecruitConditionFromRedis(recruitId);
            return RecruitConditionCacheConverter.fromCache(cachedRecruitCondition);
        } catch (CacheException ce) { // Cache Miss : DB 직접 조회
            log.info("[MySQL] recruitCondition 단건 조회 ========");

            RecruitCondition recruitCondition = getValidRecruitCondition(recruitId);
            // DB 조회 후 Caching
            recruitConditionCacheService.save(RecruitConditionCacheConverter.toCache(recruitCondition));
            return RecruitConverter.toConditionResponseDto(recruitCondition);
        }
    }

    @Transactional
    public ResponseDto createRecruitCondition(Long centerId, Long elderId, RequestDto requestDto) {
        validateElderBelongsToCenter(elderId, centerId);
        validateRequest(requestDto);

        Elder elder = getValidElder(elderId);
        Location location = getValidLocation(requestDto.getRecruitLocationId());

        RecruitCondition newRecruitCondition = RecruitConverter.toRecruitCondition(requestDto, elder, location);
        recruitConditionRepository.save(newRecruitCondition);
        recruitConditionCacheService.save(RecruitConditionCacheConverter.toCache(newRecruitCondition));

        return RecruitConverter.toConditionResponseDto(newRecruitCondition);
    }

    @Transactional
    public void updateRecruitCondition(Long centerId, Long elderId, Long recruitConditionId, RequestDto requestDto) {
        validateElderBelongsToCenter(elderId, centerId);
        validateRequest(requestDto);

        RecruitCondition recruitCondition = getValidRecruitCondition(recruitConditionId);
        Location location = getValidLocation(requestDto.getRecruitLocationId());

        recruitCondition.update(requestDto, location);
        recruitConditionRepository.save(recruitCondition);
        recruitConditionCacheService.save(RecruitConditionCacheConverter.toCache(recruitCondition));

        applicationEventPublisher.publishEvent(new RecruitConditionChangedEvent(this, recruitConditionId));
    }

    @Transactional
    public void deleteRecruitCondition(Long centerId, Long elderId, Long recruitConditionId) {
        validateElderBelongsToCenter(elderId, centerId);

        RecruitCondition recruitCondition = getValidRecruitCondition(recruitConditionId);
        // DB 삭제
        recruitConditionRepository.delete(recruitCondition);
        // Redis 삭제
        recruitConditionCacheService.deleteByRecruitConditionId(recruitConditionId);
    }

    private RecruitCondition getValidRecruitCondition(Long recruitConditionId) {
        return recruitConditionRepository.findWithRecruitTimesById(recruitConditionId)
                .orElseThrow(() -> new GlobalException(ErrorCode.RECRUIT_NOT_FOUND));
    }

    private Elder getValidElder(Long elderId) {
        return elderRepository.findById(elderId)
                .orElseThrow(() -> new GlobalException(ErrorCode.ELDER_NOT_FOUND));
    }

    private Location getValidLocation(Long locationId) {
        return locationRepository.findByLocationId(locationId)
                .orElseThrow(() -> new GlobalException(ErrorCode.LOCATION_NOT_FOUND));
    }

    private void validateElderBelongsToCenter(Long elderId, Long centerId) {
        Elder elder = elderRepository.findById(elderId)
                .orElseThrow(() -> new GlobalException(ErrorCode.ELDER_NOT_FOUND));
        if (!elder.getCenter().getCenterId().equals(centerId)) {
            throw new GlobalException(ErrorCode.ELDER_NOT_BELONG_TO_CENTER);
        }
    }

    private void validateRequest(RequestDto requestDto) {
        if (requestDto.getRecruitTimes() == null || requestDto.getRecruitTimes().isEmpty()) {
            throw new RecruitException(ErrorCode.RECRUIT_TIME_INVALID);
        }

        for (RequestTimeDto requestTimeDto : requestDto.getRecruitTimes()) {
            long dailyHour = Duration.between(
                            convertToLocalTime(requestTimeDto.getStartTime()),
                            convertToLocalTime(requestTimeDto.getEndTime()))
                    .toHours();
            log.info("하루 근무 시간: {}시간", dailyHour);
            recruitLaborLawValidator.validateMinimumWage(requestDto.getDesiredHourlyWage());
            recruitLaborLawValidator.validateWorkingHours(dailyHour);
        }
    }

    private LocalTime convertToLocalTime(Long time) {
        return LocalTime.MIN.plusMinutes(time * 30);
    }

    private RecruitConditionCache getRecruitConditionFromRedis(Long recruitConditionId) throws CacheException{
        return recruitConditionCacheService.getByRecruitConditionId(recruitConditionId);
    }
}
