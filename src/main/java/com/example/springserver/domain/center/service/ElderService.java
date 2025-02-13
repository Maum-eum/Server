package com.example.springserver.domain.center.service;

import com.example.springserver.domain.center.dto.request.ElderRequestDto.UpdateRequestDto;
import com.example.springserver.domain.center.entity.Center;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ElderService {

    private final ElderRepository elderRepository;
    private final CenterRepository centerRepository;

    @Transactional
    public Elder createElder(Long centerId, CreateRequestDto createDto, boolean isTemporary) {

        Center center = centerRepository.findById(centerId)
                .orElseThrow(() -> new CenterException(ErrorCode.CENTER_NOT_FOUND));

        // 필수 입력 필드 검증
        validateElderFields(createDto);

        Elder createdElder = elderRepository.save(ElderConverter.toSaveElder(createDto, isTemporary, center));
        createdElder.changeCenter(center);

        return createdElder;
    }

    @Transactional
    public List<Elder> getElderList(Long centerId, boolean isTemporary) {
        isValidCenter(centerId);
        return elderRepository.findByIsTemporary(isTemporary);
    }

    public Elder getElderDetail(Long centerId, Long elderId, boolean isTemporary) {
        isValidCenter(centerId);
        isValidElder(centerId);
        return elderRepository.findByElderIdAndIsTemporary(elderId, isTemporary);
    }

    @Transactional
    public Elder updateElder(Long centerId, Long elderId, UpdateRequestDto updateRequestDto) {

        Center center = centerRepository.findById(centerId)
                .orElseThrow(() -> new CenterException(ErrorCode.CENTER_NOT_FOUND));

        Elder existingElder = elderRepository.findById(elderId)
                .orElseThrow(() -> new ElderException(ErrorCode.ELDER_NOT_FOUND));


        // 수정 요청한 센터, 어르신 정보 일치 여부 검증
        if (!existingElder.getCenter().getCenterId().equals(centerId)) {
            throw new ElderException(ErrorCode.ELDER_NOT_BELONG_TO_CENTER);
        }

        existingElder.setName(updateRequestDto.getName());
        existingElder.setRate(updateRequestDto.getRate());
        existingElder.setImgUrl(updateRequestDto.getImgUrl());
        existingElder.setWeight(updateRequestDto.getWeight());

        return elderRepository.save(existingElder);
    }

    @Transactional
    public Elder deleteElder(Long centerId, Long elderId) {

        Elder elder = isValidElder(elderId);

        Center center = centerRepository.findById(centerId)
                .orElseThrow(() -> new CenterException(ErrorCode.CENTER_NOT_FOUND));

        // 센터에 속하지 않는 어르신 삭제 방지
        if (!elder.getCenter().getCenterId().equals(centerId)) {
            throw new ElderException(ErrorCode.ELDER_NOT_BELONG_TO_CENTER);
        }

        // 양방향 연관관계 해제
        center.getElders().remove(elder);
        elder.setCenter(null);

        // DB에서 어르신 삭제
        elderRepository.delete(elder);

        return elder;
    }

    public void isValidCenter(Long centerId) { // 센터 유효성 체크
        centerRepository.findById(centerId)
                .orElseThrow(() -> new ElderException(ErrorCode.CENTER_NOT_FOUND));
    }

    public Elder isValidElder(Long elderId) { // 어르신 유효성 체크
        return elderRepository.findById(elderId)
                .orElseThrow(() -> new ElderException(ErrorCode.ELDER_NOT_FOUND));
    }

    private void validateElderFields(CreateRequestDto createRequestDto) { // 필수 항목 입력 체크
        if (createRequestDto.getName() == null || createRequestDto.getGender() == null || createRequestDto.getBirth() == null) {
            throw new ElderException(ErrorCode.INVALID_ELDER_DATA);
        }
    }
}
