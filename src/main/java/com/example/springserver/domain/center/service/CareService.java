package com.example.springserver.domain.center.service;

import com.example.springserver.domain.center.dto.response.CareResponseDto.ResponseDto;
import com.example.springserver.domain.center.entity.Elder;
import com.example.springserver.domain.center.repository.CareRepository;
import com.example.springserver.domain.center.repository.ElderRepository;
import com.example.springserver.domain.location.entity.Location;
import com.example.springserver.global.apiPayload.format.ElderException;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.CareException;
import com.example.springserver.domain.center.converter.CareConverter;
import com.example.springserver.domain.center.entity.Care;
import com.example.springserver.domain.center.dto.request.CareRequestDto.RequestDto;
import com.example.springserver.repository.location.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CareService {

    private final CareRepository careRepository;
    private final LocationRepository locationRepository;
    private final ElderRepository elderRepository;

    public Care getCareInfo(Long centerId, Long elderId, Long careId) {
        isValidCenter(elderId, centerId);
        return careRepository.findById(careId)
                .orElseThrow(() -> new CareException((ErrorCode.CARE_INFO_NOT_FOUND)));
    }

    @Transactional
    public ResponseDto createCareInfo(Long centerId, Long elderId, RequestDto createRequestDto) {

        // 요청 데이터 검증
        isValidCenter(elderId, centerId);

        Elder elder = getElderById(elderId);
        Care care = saveOrUpdateCareInfo(null, elder, createRequestDto);

        return CareConverter.toConditionResponseDto(care);
    }

    @Transactional
    public void updateCareInfo(Long centerId, Long elderId, Long careId, RequestDto requestDto) {

        // 요청 데이터 검증
        isValidCenter(elderId, centerId);

        Elder elder = getElderById(elderId);
        saveOrUpdateCareInfo(careId, elder, requestDto);
    }

    @Transactional
    public void deleteCareInfo(Long centerId, Long elderId, Long careId) {
        isValidCenter(elderId, centerId);

        Care care = careRepository.findById(careId)
                .orElseThrow(() -> new CareException(ErrorCode.CARE_INFO_NOT_FOUND));

        careRepository.delete(care);
    }

    private Care saveOrUpdateCareInfo(Long careId, Elder elder, RequestDto requestDto) {
        Location location = locationRepository.findByLocationId(requestDto.getCareLocation());
        Care care;

        if (careId == null) { // create
            care = CareConverter.toCare(requestDto, elder, location);
        } else { // update
            care = careRepository.findById(careId)
                    .orElseThrow(() -> new CareException(ErrorCode.CARE_INFO_NOT_FOUND));
            care.update(requestDto, location);
        }

        careRepository.save(care);
        return care;
    }

    private Elder getElderById(Long elderId) {
        return elderRepository.findById(elderId)
                .orElseThrow(() -> new ElderException(ErrorCode.ELDER_NOT_FOUND));
    }

    private void isValidCenter(Long elderId, Long centerId) {
        elderRepository.findByElderIdAndCenter_CenterId(elderId, centerId)
                .orElseThrow(() -> new ElderException(ErrorCode.CENTER_NOT_FOUND));
    }
}
