package com.example.springserver.domain.center.service;

import com.example.springserver.domain.center.dto.request.RecruitRequestDto.CreateReqDto;
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
import com.example.springserver.domain.center.dto.request.RecruitRequestDto.UpdateRequestDto;
import com.example.springserver.global.validation.validator.RecruitLaborLawValidator;
import com.example.springserver.domain.center.repository.RecruitCondRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public ResponseDto createRecruit(Long centerId, Long elderId, CreateReqDto createRequestDto) {
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
    public void updateRecruitCondition(Long centerId, Long elderId, Long recruitConditionId, UpdateRequestDto requestDto) {
        isValidCenter(elderId, centerId);

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

//    // 근로기준법 시간 검증 메서드
//    public void isValidRecruitCondition(CreateReqDto createRecruitDto) {
//        // 각 필드에 대한 유효성 검사를 진행합니다.
//
//        long dailyHour = Duration.between(createRecruitDto.getRecruitTimes().getStartTime(), createRecruitDto.getRecruitTime().getEndTime()).toHours();
//
//        recruitLaborLawValidator.validateMinimumWage(createRecruitDto.getRecruitCondition().getDesiredHourlyWage()); // 최저임금 검증
//        recruitLaborLawValidator.validateWorkingHours(dailyHour); // 근로시간 검증
//    }
}