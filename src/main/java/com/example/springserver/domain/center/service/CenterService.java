package com.example.springserver.domain.center.service;

import com.example.springserver.domain.center.entity.Center;
import com.example.springserver.domain.center.repository.CenterRepository;
import com.example.springserver.global.apiPayload.format.CenterException;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CenterService {

    private final CenterRepository centerRepository;

    public Center searchCenter(String centerName) {
        return centerRepository.findByCenterName(centerName)
                .orElseThrow(() -> new CenterException(ErrorCode.CENTER_NOT_FOUND));
    }
}
