package com.example.springserver.domain.caregiver.service;

import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDTO;
import com.example.springserver.domain.caregiver.entity.Caregiver;
import com.example.springserver.domain.caregiver.repository.CaregiverRepository;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.GlobalException;
import com.example.springserver.global.security.util.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CareGiverService {

    private final CaregiverRepository caregiverRepository;


    public Caregiver getUserInfo(CustomUserDetails request) {
        String username = request.getUsername();
        try{
            return caregiverRepository.findByUsername(username);
        }catch (GlobalException e) {
            throw new GlobalException(ErrorCode.USER_NOT_FOUND);
        }
    }

    @Transactional
    public Caregiver updateUserInfo(CaregiverRequestDTO.UpdateCaregiverReq request) {
        Caregiver caregiver = caregiverRepository.findByUsername(request.getUsername());
        if(caregiver==null)
            throw new GlobalException(ErrorCode.USER_NOT_FOUND);

        caregiver.setUpdate(request);

        try{
            return caregiverRepository.save(caregiver);
        }catch (GlobalException e) {
            throw new GlobalException(ErrorCode.USER_NOT_FOUND);
        }
    }
}
