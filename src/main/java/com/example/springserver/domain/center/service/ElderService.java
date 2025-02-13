package com.example.springserver.domain.center.service;

import com.example.springserver.global.apiPayload.format.ElderException;
import com.example.springserver.domain.center.converter.ElderConverter;
import com.example.springserver.domain.center.entity.Elder;
import com.example.springserver.domain.center.dto.request.ElderRequestDto.CreateRequestDto;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.repository.center.CenterRepository;
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
        isValidCenter(centerId);
        return elderRepository.save(ElderConverter.toSaveElder(createDto, isTemporary));
    }

    @Transactional
    public List<Elder> getElderList(Long centerId, boolean isTemporary) {
        isValidCenter(centerId);
        return elderRepository.findByCenterIdAndIsTemporary(centerId, isTemporary);
    }

    public Elder getElderDetail(Long centerId, Long elderId, boolean isTemporary) {
        isValidCenter(centerId);
        isValidElder(centerId);
        return elderRepository.findByCenterIdAndElderIdAndIsTemporary(centerId, elderId, isTemporary);
    }

    @Transactional
    public void deleteElder(Long centerId, Long elderId) {
        Elder elder = isValidElder(elderId);

        // 센터에 속하지 않는 어르신 삭제 방지
        if (!elder.getCenter().getId().equals(centerId))
            throw new ElderException(ErrorCode.ELDER_NOT_BELONG_TO_CENTER);

        elderRepository.delete(elder);
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
        log.info("name은 = {} gender는 = {} birth는 = {}", createRequestDto.getName(), createRequestDto.getGender(), createRequestDto.getBirth());
        if (createRequestDto.getName() == null || createRequestDto.getGender() == null || createRequestDto.getBirth() == null) {
            throw new ElderException(ErrorCode.INVALID_ELDER_DATA);
        }
    }
}
