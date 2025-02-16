package com.example.springserver.domain.match.service;

import com.example.springserver.domain.match.dto.response.MatchResponseDTO.CaregiverRecommendedList;
import com.example.springserver.domain.match.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchService {

    private final MatchRepository matchRepository;


    public CaregiverRecommendedList getCareGiverRecommendList(Long elderId, Long recruitId) {
        return null;
    }
}
