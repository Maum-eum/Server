package com.example.springserver.domain.caregiver.service;

import com.example.springserver.domain.caregiver.converter.CaregiverConverter;
import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDto.*;
import com.example.springserver.domain.caregiver.entity.Caregiver;
import com.example.springserver.domain.caregiver.entity.Certificate;
import com.example.springserver.domain.caregiver.entity.Experience;
import com.example.springserver.domain.caregiver.repository.CaregiverRepository;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.GlobalException;
import com.example.springserver.global.security.util.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        updateCertificates(caregiver, request.getCertificateRequestDTOList());
        updateExperiences(caregiver, request.getExperienceRequestDTOList());

        caregiver.setCar(request.getCar());
        caregiver.setAddress(request.getAddress());
        caregiver.setImg(request.getImg());
        caregiver.setEducation(request.getEducation());
        caregiver.setContact(request.getContact());
        caregiver.setIntro(request.getIntro());

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

    private void updateCertificates(Caregiver caregiver, List<CertificateRequestDTO> newCertificates) {
        List<Certificate> updatedCertificates = newCertificates.stream()
                .map(dto -> CaregiverConverter.toCertificate(caregiver, dto))
                .toList();

        for (Certificate newCert : updatedCertificates) {

            if (!caregiver.getCertificates().contains(newCert)) {
                caregiver.getCertificates().add(newCert);
            }
        }

        caregiver.getCertificates().removeIf(cert -> !updatedCertificates.contains(cert));
    }

    private void updateExperiences(Caregiver caregiver, List<ExperienceRequestDTO> newExperiences) {
        List<Experience> updatedExperiences = newExperiences.stream()
                .map(dto -> CaregiverConverter.toExperience(caregiver, dto))
                .toList();

        for (Experience newExp : updatedExperiences) {
            if (!caregiver.getExperiences().contains(newExp)) {
                caregiver.getExperiences().add(newExp);
            }
        }

        caregiver.getExperiences().removeIf(exp -> !updatedExperiences.contains(exp));
    }
}
