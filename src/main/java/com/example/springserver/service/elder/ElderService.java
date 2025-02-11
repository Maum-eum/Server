package com.example.springserver.service.elder;

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
        return elderRepository.save(ElderConverter.toElder(createDto));
    }




}
