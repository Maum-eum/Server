package com.example.springserver.service.elder;

import ch.qos.logback.core.spi.ErrorCodes;
import com.example.springserver.apiPayload.code.status.ErrorStatus;
import com.example.springserver.apiPayload.exception.elder.ElderException;
import com.example.springserver.converter.elder.ElderConverter;
import com.example.springserver.domain.entity.elder.ElderEntity;
import com.example.springserver.dto.elder.ElderRequestDto.CreateReqDto;
import com.example.springserver.repository.elder.ElderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ElderService {

    private final ElderRepository elderRepository;

    @Transactional
    public ElderEntity createElder(Long centerId, CreateReqDto createDto) {

        isValidateCenter(centerId);
        return elderRepository.save(ElderConverter.toElder(createDto));
    }

    public boolean isValidateCenter(Long CenterId) {
        // ceterRepository로 조회 후 예외 처리
        throw new ElderException(ErrorStatus.CENTER_NOT_FOUND);
    }

}
