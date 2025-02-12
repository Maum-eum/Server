package com.example.springserver.service.location;

import com.example.springserver.apiPayload.code.status.ErrorStatus;
import com.example.springserver.apiPayload.exception.GeneralException;
import com.example.springserver.domain.entity.location.Sigungu;
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
            throw new GeneralException(ErrorStatus.BAD_REQUEST);
        return sigunguRepository.findBySidoId(sidoId);
    }
}
