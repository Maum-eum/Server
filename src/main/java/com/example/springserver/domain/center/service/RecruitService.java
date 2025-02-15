package com.example.springserver.domain.center.service;

import com.example.springserver.domain.center.dto.request.RecruitRequestDto.RequestTimeDto;
import com.example.springserver.domain.center.dto.response.RecruitResponseDto.ResponseDto;
import com.example.springserver.domain.center.entity.Elder;
import com.example.springserver.domain.center.entity.RecruitTime;
import com.example.springserver.domain.center.repository.ElderRepository;
import com.example.springserver.domain.center.repository.RecruitTimeRepository;
import com.example.springserver.global.apiPayload.format.ElderException;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.RecruitException;
import com.example.springserver.domain.center.converter.RecruitConverter;
import com.example.springserver.domain.center.entity.RecruitCondition;
import com.example.springserver.domain.center.dto.request.RecruitRequestDto.RequestDto;
import com.example.springserver.global.validation.validator.RecruitLaborLawValidator;
import com.example.springserver.domain.center.repository.RecruitCondRepository;
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
        isValidCenter(elderId, centerId);

        Elder elder = elderRepository.findById(elderId)
                .orElseThrow(() -> new ElderException(ErrorCode.ELDER_NOT_FOUND));

        RecruitCondition recruitCondition = RecruitConverter.toRecruitCondition(createRequestDto, elder);

        for (RequestTimeDto time : createRequestDto.getRecruitTimes()) {
            RecruitTime recruitTime = RecruitConverter.toRecruitTime(time, recruitCondition);
            recruitCondition.addRecruitTime(recruitTime); // 연관관계 매핑
        }

        elder.getRecruitConditions().add(recruitCondition);

        recruitCondRepository.save(recruitCondition);
        return RecruitConverter.toConditionResponseDto(recruitCondition);
    }

    @Transactional
    public void updateRecruitCondition(Long centerId, Long elderId, Long recruitConditionId, RequestDto requestDto) {

        isValidCenter(elderId, centerId);
        isValidRecruitCondition(requestDto);

        RecruitCondition recruitCondition = recruitCondRepository.findById(recruitConditionId)
                .orElseThrow(() -> new RecruitException(ErrorCode.RECRUIT_NOT_FOUND));

        recruitTimeRepository.deleteByRecruitConditionId(recruitConditionId);
        recruitCondition.getRecruitTimes().clear();

        recruitCondition.update(requestDto);

        for (RequestTimeDto time : requestDto.getRecruitTimes()) {
            RecruitTime recruitTime = RecruitConverter.toRecruitTime(time, recruitCondition);
            recruitCondition.addRecruitTime(recruitTime); // 연관관계 매핑
        }
        recruitCondRepository.save(recruitCondition);
    }

    @Transactional
    public void deleteRecruitCondition(Long centerId, Long elderId, Long recruitConditionId) {
        isValidCenter(elderId, centerId);

        RecruitCondition recruitCondition = recruitCondRepository.findById(recruitConditionId)
                        .orElseThrow(() -> new RecruitException(ErrorCode.RECRUIT_NOT_FOUND));
        recruitCondRepository.delete(recruitCondition);
    }

    public void isValidCenter(Long elderId, Long centerId) {
        elderRepository.findByElderIdAndCenter_CenterId(elderId, centerId)
                .orElseThrow(() -> new ElderException(ErrorCode.CENTER_NOT_FOUND));
    }

    // 근로기준법 시간 검증 메서드
    public void isValidRecruitCondition(RequestDto createRecruitDto) {

        if (createRecruitDto.getRecruitTimes() == null || createRecruitDto.getRecruitTimes().isEmpty()) {
            throw new RecruitException(ErrorCode.RECRUIT_TIME_INVALID);
        }

        // 첫 번째 시간 정보로 유효성 검사 진행 (필요시 모든 recruitTimes를 검증할 수 있음)
        RequestTimeDto requestTimeDto = createRecruitDto.getRecruitTimes().get(0);
        long dailyHour = Duration.between(
                changeToLocalDateTime(
                        requestTimeDto
                                .getStartTime()),
                changeToLocalDateTime(
                        requestTimeDto
                                .getEndTime()))
                .toHours();

        // 최저임금과 근로시간 검증
        recruitLaborLawValidator.validateMinimumWage(createRecruitDto.getDesiredHourlyWage()); // 희망 급여에 대한 최저임금 검증
        recruitLaborLawValidator.validateWorkingHours(dailyHour); // 근로시간 검증
    }

    public LocalTime changeToLocalDateTime(Long time){
        return LocalTime.MIN.plusMinutes(time * 30);
    }
}