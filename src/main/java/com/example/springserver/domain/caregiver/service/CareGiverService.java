package com.example.springserver.domain.caregiver.service;

import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDto.*;
import com.example.springserver.domain.caregiver.dto.response.CaregiverResponseDto;
import com.example.springserver.domain.caregiver.dto.response.CaregiverResponseDto.*;
import com.example.springserver.domain.caregiver.dto.response.CaregiverResponseDto.WorkTimes;
import com.example.springserver.domain.caregiver.entity.Caregiver;
import com.example.springserver.domain.caregiver.entity.JobCondition;
import com.example.springserver.domain.caregiver.entity.enums.Sexual;
import com.example.springserver.domain.caregiver.repository.CaregiverRepository;
import com.example.springserver.domain.caregiver.repository.JobConditionRepository;
import com.example.springserver.domain.center.entity.*;
import com.example.springserver.domain.center.entity.enums.MatchStatus;
import com.example.springserver.domain.center.entity.enums.RecruitStatus;
import com.example.springserver.domain.center.repository.MatchRepository;
import com.example.springserver.domain.match.entity.Match;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.GlobalException;
import com.example.springserver.global.security.util.CustomUserDetails;
import com.example.springserver.service.location.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CareGiverService {

    private final CaregiverRepository caregiverRepository;

    public Caregiver getUserInfo(CustomUserDetails user) {
        Long userId = user.getId();
        return caregiverRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public Caregiver updateUserInfo(CustomUserDetails user,
                                    UpdateCaregiverReqDto request) {
        Caregiver caregiver = getById(user);
        caregiver.setUpdate(caregiver,request);
        return caregiverRepository.save(caregiver);
    }

    @Transactional
    public Boolean changeStatus(CustomUserDetails user) {
        Caregiver byId = getById(user);
        byId.setEmploymentStatus(!byId.getEmploymentStatus());
        return caregiverRepository.save(byId).getEmploymentStatus();
    }

    private Caregiver getById(CustomUserDetails user) throws GlobalException {
        return caregiverRepository.findById(user.getId()).orElseThrow(()-> new GlobalException(ErrorCode.USER_NOT_FOUND));
    }
}
