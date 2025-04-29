package com.example.springserver.domain.caregiver.service;

import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDto.CaregiverUpdateRequest;
import com.example.springserver.domain.caregiver.entity.Caregiver;
import com.example.springserver.domain.caregiver.repository.CaregiverRepository;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.GlobalException;
import com.example.springserver.global.security.util.CustomUserDetails;
import com.example.springserver.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CareGiverService {

    private final CaregiverRepository caregiverRepository;
    private final S3Service s3Service;

    public Caregiver getUserInfo(CustomUserDetails user) {
        return getValidCaregiver(user.getId());
    }

    @Transactional
    public Caregiver updateUserInfo(CustomUserDetails user, CaregiverUpdateRequest request, MultipartFile profileImg) {
        Caregiver caregiver = getValidCaregiver(user.getId());

        // 이미지 적용
        String imgUrl = (profileImg != null)
                ? s3Service.uploadFileImage(profileImg)
                : request.getImg();

        caregiver.updateProfile(imgUrl, request.getBasicInfo());
        caregiver.updateCertificates(request.getCertificateRequestList());
        caregiver.updateExperiences(request.getExperienceRequestList());

        return caregiver;
    }

    private Caregiver getValidCaregiver(Long userId) throws GlobalException {
        return caregiverRepository.findById(userId)
                .orElseThrow(()-> new GlobalException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public Boolean updateStatus(CustomUserDetails user) {
        Caregiver caregiver = getValidCaregiver(user.getId());
        caregiver.changeEmploymentStatus(!caregiver.getEmploymentStatus());
        return caregiverRepository.save(caregiver).getEmploymentStatus();
    }
}
