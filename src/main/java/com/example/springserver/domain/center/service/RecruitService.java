package com.example.springserver.domain.center.service;

import com.example.springserver.domain.center.cache.RecruitConditionCache;
import com.example.springserver.domain.center.cache.RecruitConditionCacheConverter;
import com.example.springserver.domain.center.converter.RecruitConverter;
import com.example.springserver.domain.center.dto.request.RecruitRequestDto.Request;
import com.example.springserver.domain.center.dto.response.RecruitResponseDto.Response;
import com.example.springserver.domain.center.entity.Elder;
import com.example.springserver.domain.center.entity.RecruitCondition;
import com.example.springserver.domain.center.repository.ElderRepository;
import com.example.springserver.domain.center.repository.RecruitConditionRepository;
import com.example.springserver.domain.center.service.cache.RecruitConditionCacheService;
import com.example.springserver.domain.location.entity.Location;
import com.example.springserver.global.apiPayload.format.CacheException;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.GlobalException;
import com.example.springserver.global.validation.validator.RecruitLaborLawValidator;
import com.example.springserver.repository.location.LocationRepository;
import com.example.springserver.service.event.RecruitConditionChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<Long> findAllRecCond(){
        return recruitConditionRepository.findAllRecuitIds();
    }

    public List<Response> getRecruitConditionList(Long centerId, Long elderId) {
        validateElderBelongsToCenter(elderId, centerId);

        try { // Cache Hit : Redis 조회
            log.info("[CACHE HIT] recruitCondition 조회 ======== ");
            List<RecruitConditionCache> cachedList = recruitConditionCacheService.getByElderIdFromRedis(elderId);
            return RecruitConditionCacheConverter.fromCacheList(cachedList);
        } catch(CacheException ce) { // Cache Miss : DB 직접 조회
            log.warn("[CACHE MISS] jobCondition 캐시 조회 불가 ======== ");

            List<RecruitCondition> conditions = recruitConditionRepository.findWithRecruitTimesByElderId(elderId);
            recruitConditionCacheService.saveAll(RecruitConditionCacheConverter.toCacheList(conditions));
            return RecruitConverter.toListResponseDto(conditions);
        }
    }

    // Read Through 캐싱 (Redis + Local Cache)
    public Response getRecruitCondition(Long centerId, Long elderId, Long recruitId) {
        validateElderBelongsToCenter(elderId, centerId);
        RecruitConditionCache cache = null;

        try {
            cache = getRecruitConditionFromRedis(recruitId);
            log.info("[CACHE HIT] recruitCondition 조회 ======== ");
        } catch (CacheException ce) {
            log.warn("[CACHE MISS] recruitCondition 캐시 조회 실패 ======== ");
        }

        if (cache != null) { // null 처리
            return RecruitConditionCacheConverter.fromCache(cache);
        }

        log.info("[MySQL] recruitCondition 단건 조회 ========");
        RecruitCondition recruitCondition = getValidRecruitCondition(recruitId);

        // DB 조회 후 캐싱
        recruitConditionCacheService.save(RecruitConditionCacheConverter.toCache(recruitCondition));
        return RecruitConverter.toConditionResponseDto(recruitCondition);
    }

    @Transactional
    public Response createRecruitCondition(Long centerId, Long elderId, Request request) {
        validateElderBelongsToCenter(elderId, centerId);
        validateRequest(request);

        Elder elder = getValidElder(elderId);
        Location location = getValidLocation(request.getRecruitLocationId());

        RecruitCondition newRecruitCondition = RecruitConverter.toRecruitCondition(request, elder, location);
        recruitConditionRepository.save(newRecruitCondition);
        recruitConditionCacheService.save(RecruitConditionCacheConverter.toCache(newRecruitCondition));

        return RecruitConverter.toConditionResponseDto(newRecruitCondition);
    }

    @Transactional
    public void updateRecruitCondition(Long centerId, Long elderId, Long recruitConditionId, Request request) {
        validateElderBelongsToCenter(elderId, centerId);
        validateRequest(request);

        RecruitCondition recruitCondition = getValidRecruitCondition(recruitConditionId);
        Location location = getValidLocation(request.getRecruitLocationId());

        recruitCondition.update(request, location);
        recruitConditionRepository.save(recruitCondition);
        recruitConditionCacheService.save(RecruitConditionCacheConverter.toCache(recruitCondition));

        applicationEventPublisher.publishEvent(new RecruitConditionChangedEvent(this, recruitConditionId));
    }

    @Transactional
    public void deleteRecruitCondition(Long centerId, Long elderId, Long recruitConditionId) {
        validateElderBelongsToCenter(elderId, centerId);

        RecruitCondition recruitCondition = getValidRecruitCondition(recruitConditionId);

        recruitConditionRepository.delete(recruitCondition); // DB 삭제
        recruitConditionCacheService.deleteByRecruitConditionId(recruitConditionId); // 캐시 삭제
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
        Elder elder = getValidElder(elderId);
        if (!elder.getCenter().getCenterId().equals(centerId)) {
            throw new GlobalException(ErrorCode.ELDER_NOT_BELONG_TO_CENTER);
        }
    }

    private void validateRequest(Request request) {
        recruitLaborLawValidator.validateRecruitRequest(request);
    }

    private RecruitConditionCache getRecruitConditionFromRedis(Long recruitConditionId) throws CacheException{
        return recruitConditionCacheService.getByRecruitConditionId(recruitConditionId);
    }
}