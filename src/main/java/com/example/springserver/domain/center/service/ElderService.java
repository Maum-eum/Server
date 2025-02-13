package com.example.springserver.domain.center.service;

import com.example.springserver.domain.center.dto.request.ElderRequestDto;
import com.example.springserver.domain.center.dto.request.ElderRequestDto.RequestDto;
import com.example.springserver.domain.center.dto.request.ElderRequestDto.UpdateRequestDto;
import com.example.springserver.domain.center.entity.Center;
import com.example.springserver.domain.center.entity.enums.ElderRate;
import com.example.springserver.global.apiPayload.format.CenterException;
import com.example.springserver.global.apiPayload.format.ElderException;
import com.example.springserver.domain.center.converter.ElderConverter;
import com.example.springserver.domain.center.entity.Elder;
import com.example.springserver.domain.center.dto.request.ElderRequestDto.CreateRequestDto;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.domain.center.repository.CenterRepository;
import com.example.springserver.domain.center.repository.ElderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Request;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ElderService {

    private final ElderRepository elderRepository;
    private final CenterRepository centerRepository;

    @Transactional
    public Elder createElder(Long centerId, CreateRequestDto createDto, boolean isTemporary) {

        Center validCenter = getValidCenter(centerId);

        // 어르신 정보 필수 입력 필드 검증 (일반 저장 / 임시 저장)
        validateElderFields(isTemporary, createDto);

        Elder createdElder = elderRepository.save(ElderConverter.toSaveElder(createDto, isTemporary, validCenter));
        createdElder.changeCenter(validCenter);

        return createdElder;
    }

    @Transactional
    public List<Elder> getElderList(Long centerId, boolean isTemporary) {
        getValidCenter(centerId);
        return elderRepository.findByIsTemporary(isTemporary);
    }

    public Elder getElderDetail(Long centerId, Long elderId, boolean isTemporary) {
        getValidElder(elderId, centerId);
        return elderRepository.findByElderIdAndIsTemporary(elderId, isTemporary);
    }

    @Transactional
    public void updateElder(Long centerId, Long elderId, RequestDto updateRequestDto) {
        Elder validElder = getValidElder(elderId, centerId);

        updateSelectedElderInfo(validElder, updateRequestDto);
    }

    @Transactional
    public Elder deleteElder(Long centerId, Long elderId) {

        Elder validElder = getValidElder(elderId, centerId);
        Center validCenter = getValidCenter(centerId);

        removeElderFromCenter(validElder, validCenter);

        // DB에서 어르신 삭제
        elderRepository.delete(validElder);

        return validElder;
    }

    private void updateSelectedElderInfo(Elder elder, RequestDto updateRequestDto) {

        // 변경 요청이 들어온 필드만 수정
        updateChangedElderInfo(elder::setName, updateRequestDto.getName());
        updateChangedElderInfo(elder::setGender, updateRequestDto.getGender());
        updateChangedElderInfo(elder::setBirth, updateRequestDto.getBirth());
        updateChangedElderInfo(elder::setRate, updateRequestDto.getRate());
        updateChangedElderInfo(elder::setImgUrl, updateRequestDto.getImgUrl());
        updateChangedElderInfo(elder::setWeight, updateRequestDto.getWeight());

        if (updateRequestDto.getCenterName() != null) {
            updateElderCenter(elder, updateRequestDto);
        }
    }

    private void updateElderCenter(Elder elder, RequestDto updateRequestDto) {
        Center currentCenter = elder.getCenter();

        currentCenter.getElders().remove(elder); // 기존 센터에서 제거

        Center newCenter = centerRepository.findByCenterName(updateRequestDto.getCenterName())
                .orElseThrow(() -> new CenterException(ErrorCode.CENTER_NOT_FOUND));

        elder.changeCenter(newCenter);
    }

    private <T> void updateChangedElderInfo(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }

    // 센터에서 어르신 제거 (양방향 연관관계 해제)
    private void removeElderFromCenter(Elder elder, Center center) {
        center.getElders().remove(elder);
    }

    public Center getValidCenter(Long centerId) { // 센터 유효성 체크
        return centerRepository.findById(centerId)
                .orElseThrow(() -> new ElderException(ErrorCode.CENTER_NOT_FOUND));
    }

    public Elder getValidElder(Long elderId, Long centerId) { // 어르신 유효성 체크
        return elderRepository.findByElderIdAndCenter_CenterId(elderId, centerId)
                .orElseThrow(() -> new ElderException(ErrorCode.ELDER_NOT_BELONG_TO_CENTER));
    }

    private void validateElderFields(boolean isTemporary, CreateRequestDto createRequestDto) { // 필수 항목 입력 체크

        // 임시 저장인 경우 3가지 필수 입력 항목 검증
        if (createRequestDto.getName() == null || createRequestDto.getGender() == null || createRequestDto.getBirth() == null) {
            throw new ElderException(ErrorCode.INVALID_TEMP_ELDER_DATA);
        }

        // 정식 저장인 경우 나머지 필수 입력 항목 검증
        if (!isTemporary) {
            if (createRequestDto.getRate() == null || createRequestDto.getImgUrl() == null || createRequestDto.getWeight() == null) {
                throw new ElderException(ErrorCode.INVALID_ELDER_DATA);
            }
        }
    }
}
