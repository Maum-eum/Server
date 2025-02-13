package com.example.springserver.service.recruit;

import com.example.springserver.global.apiPayload.format.ElderException;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.RecruitException;
import com.example.springserver.domain.center.converter.RecruitCondConverter;
import com.example.springserver.domain.center.entity.RecruitCondition;
import com.example.springserver.domain.center.dto.request.RecruitCondRequestDto.CreateReqDto;
import com.example.springserver.domain.center.dto.request.RecruitCondRequestDto.CreateReqTimeDto;
import com.example.springserver.domain.center.dto.request.RecruitCondRequestDto.UpdateRequestDto;
import com.example.springserver.global.validation.validator.RecruitLaborLawValidator;
import com.example.springserver.domain.center.repository.CenterRepository;
import com.example.springserver.repository.recruit.RecruitCondRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecruitService {

    private final RecruitCondRepository recruitCondRepository;
    private final CenterRepository centerRepository;

    private final RecruitLaborLawValidator recruitLaborLawValidator;

    @Transactional
    public RecruitCondition createRecruit(Long centerId, CreateReqDto createDto, CreateReqTimeDto createReqTimeDto) {

        isValidCenter(centerId);
        isValidRecruitCondition(createDto, createReqTimeDto);
        return recruitCondRepository.save(RecruitCondConverter.toRecruitCondition(createDto));
    }

    @Transactional
    public RecruitCondition updateRecruitCondition(Long centerId, Long recruitConditionId, UpdateRequestDto requestDto) {
        isValidCenter(centerId);

        RecruitCondition recruitCond = recruitCondRepository.findById(recruitConditionId)
                .orElseThrow(() -> new RecruitException(ErrorCode.RECRUIT_NOT_FOUND));

        return updateRecruitConditionEntity(recruitCond, requestDto); // 상태 변경
    }

    public String deleteRecruitCondition(Long centerId, Long recruitConditionId) {
        isValidCenter(centerId);

        RecruitCondition recruitCondition = recruitCondRepository.findById(recruitConditionId)
                        .orElseThrow(() -> new RecruitException(ErrorCode.RECRUIT_NOT_FOUND));
        recruitCondRepository.delete(recruitCondition);

        return "삭제 완료";
    }

    public void isValidCenter(Long centerId) { // 센터 유효성 체크
        centerRepository.findById(centerId)
                .orElseThrow(() -> new ElderException(ErrorCode.CENTER_NOT_FOUND));
    }

    public RecruitCondition updateRecruitConditionEntity(RecruitCondition recruitCondition, UpdateRequestDto requestDto) {

        /* TODO : 근무 조건 수정 허용할 필드 전달받아서 추가하기 */
        recruitCondition.setCareType(requestDto.getCareType());

        return recruitCondition;
    }

    // 근로기준법 시간 검증 메서드
    public void isValidRecruitCondition(CreateReqDto createReqDto, CreateReqTimeDto createReqTimeDto) {
        // 각 필드에 대한 유효성 검사를 진행합니다.

        int dailyHour = createReqTimeDto.getEndTime() - createReqTimeDto.getStartTime();
        recruitLaborLawValidator.validateMinimumWage(createReqDto.getDesiredHourlyWage()); // 최저임금 검증
        recruitLaborLawValidator.validateWorkingHours(dailyHour); // 근로시간 검증
    }
}