package com.example.springserver.domain.center.service;

import com.example.springserver.domain.center.converter.RecruitConverter;
import com.example.springserver.domain.center.dto.request.RecruitRequestDto.RequestDto;
import com.example.springserver.domain.center.dto.request.RecruitRequestDto.RequestTimeDto;
import com.example.springserver.domain.center.dto.response.RecruitResponseDto.ResponseDto;
import com.example.springserver.domain.center.entity.Elder;
import com.example.springserver.domain.center.entity.RecruitCondition;
import com.example.springserver.domain.center.entity.RecruitTime;
import com.example.springserver.domain.center.repository.*;
import com.example.springserver.domain.location.entity.Location;
import com.example.springserver.global.apiPayload.format.CenterException;
import com.example.springserver.global.apiPayload.format.ElderException;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.RecruitException;
import com.example.springserver.global.validation.validator.RecruitLaborLawValidator;
import com.example.springserver.repository.location.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final RecruitCondRepository recruitCondRepository;
    private final RecruitTimeRepository recruitTimeRepository;
    private final LocationRepository locationRepository;
    private final CenterRepository centerRepository;
    private final ElderRepository elderRepository;

    private final RecruitLaborLawValidator recruitLaborLawValidator;

    public List<RecruitCondition> getRecruitConditionList(Long centerId, Long elderId) {
        isValidCenter(elderId, centerId);
        return recruitCondRepository.findWithRecruitTimesByElderId(elderId);
    }

    public RecruitCondition getRecruitCondition(Long centerId, Long elderId, Long recruitId) {
        isValidCenter(elderId, centerId);
        return recruitCondRepository.findWithRecruitTimesById(recruitId)
                .orElseThrow(() -> new RecruitException(ErrorCode.RECRUIT_NOT_FOUND));
    }

    @Transactional
    public ResponseDto createRecruit(Long centerId, Long elderId, RequestDto createRequestDto) {

        // 요청 데이터 검증
        isValidCenter(elderId, centerId);
        isValidRecruitCondition(createRequestDto);

        Elder elder = getElderById(elderId);
        RecruitCondition recruitCondition = saveOrUpdateRecruitCondition(null, elder, createRequestDto);

        return RecruitConverter.toConditionResponseDto(recruitCondition);
    }

    @Transactional
    public void updateRecruitCondition(Long centerId, Long elderId, Long recruitConditionId, RequestDto requestDto) {

        // 요청 데이터 검증
        isValidCenter(elderId, centerId);
        isValidRecruitCondition(requestDto);

        Elder elder = getElderById(elderId);
        saveOrUpdateRecruitCondition(recruitConditionId, elder, requestDto);
    }

    @Transactional
    public void deleteRecruitCondition(Long centerId, Long elderId, Long recruitConditionId) {
        isValidCenter(elderId, centerId);

        RecruitCondition recruitCondition = recruitCondRepository.findById(recruitConditionId)
                .orElseThrow(() -> new RecruitException(ErrorCode.RECRUIT_NOT_FOUND));

        recruitCondRepository.delete(recruitCondition);
    }

    private RecruitCondition saveOrUpdateRecruitCondition(Long recruitConditionId, Elder elder, RequestDto requestDto) {
        Location location = locationRepository.findByLocationId(requestDto.getRecruitLocation());
        RecruitCondition recruitCondition;

        if (recruitConditionId == null) { // create
            recruitCondition = RecruitConverter.toRecruitCondition(requestDto, elder, location);
        } else { // update
            recruitCondition = recruitCondRepository.findById(recruitConditionId)
                    .orElseThrow(() -> new RecruitException(ErrorCode.RECRUIT_NOT_FOUND));

            recruitCondition.update(requestDto, location);
            recruitTimeRepository.deleteByRecruitConditionId(recruitConditionId);
            recruitCondition.getRecruitTimes().clear();
        }

        recruitCondRepository.save(recruitCondition);
        RecruitTimesMapping(recruitCondition, requestDto.getRecruitTimes());
        return recruitCondition;
    }

    private void RecruitTimesMapping(RecruitCondition recruitCondition, List<RequestTimeDto> recruitTimes) {
        recruitTimes.forEach(time -> {
            RecruitTime recruitTime = RecruitConverter.toRecruitTime(time, recruitCondition);
            recruitCondition.addRecruitTime(recruitTime);
        });
    }

    private Elder getElderById(Long elderId) {
        return elderRepository.findById(elderId)
                .orElseThrow(() -> new ElderException(ErrorCode.ELDER_NOT_FOUND));
    }

    private void isValidCenter(Long elderId, Long centerId) {
        // 센터 검증
        if (!centerRepository.existsById(centerId)) {
            throw new CenterException(ErrorCode.CENTER_NOT_FOUND);
        }
        // 어르신 검증
        if (!elderRepository.existsById(elderId)) {
            throw new ElderException(ErrorCode.ELDER_NOT_FOUND);
        }
        // 센터 - 어르신 관계 검증
        if (!elderRepository.existsByElderIdAndCenter_CenterId(elderId, centerId)) {
            throw new ElderException(ErrorCode.ELDER_NOT_BELONG_TO_CENTER);
        }
    }

    private void isValidRecruitCondition(RequestDto createRecruitDto) {
        if (createRecruitDto.getRecruitTimes() == null || createRecruitDto.getRecruitTimes().isEmpty()) {
            throw new RecruitException(ErrorCode.RECRUIT_TIME_INVALID);
        }

        for(RequestTimeDto requestTimeDto : createRecruitDto.getRecruitTimes()) {
            long dailyHour = Duration.between(
                            convertTime(requestTimeDto.getStartTime()),
                            convertTime(requestTimeDto.getEndTime()))
                    .toHours();
            log.info("현재 하루 근무 시간은 = {} - {} = {}", convertTime(requestTimeDto.getEndTime()), convertTime(requestTimeDto.getStartTime()), dailyHour);
            recruitLaborLawValidator.validateMinimumWage(createRecruitDto.getDesiredHourlyWage());
            recruitLaborLawValidator.validateWorkingHours(dailyHour);
        }
    }

    private LocalTime convertTime(Long time) {
        return LocalTime.MIN.plusMinutes(time * 30);
    }
}
