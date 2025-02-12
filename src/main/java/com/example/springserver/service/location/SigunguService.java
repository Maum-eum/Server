package com.example.springserver.service.location;

import com.example.springserver.domain.location.entity.Sigungu;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.GlobalException;
import com.example.springserver.repository.location.SigunguRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SigunguService {

    private final SigunguRepository sigunguRepository;

    public List<Sigungu> getSigunguList(Long sidoId) {
        if(sidoId == 0)
            throw new GlobalException(ErrorCode.BAD_REQUEST);
        return sigunguRepository.findBySidoId(sidoId);
    }
}
