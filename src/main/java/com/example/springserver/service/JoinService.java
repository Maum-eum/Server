package com.example.springserver.service;
import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDto.CertificateRequest;
import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDto.ExperienceRequest;
import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDto.CaregiverSignupRequest;
import com.example.springserver.domain.caregiver.repository.CertificateRepository;
import com.example.springserver.domain.caregiver.repository.ExperienceRepository;
import com.example.springserver.domain.center.converter.AdminConverter;
import com.example.springserver.domain.caregiver.converter.CaregiverConverter;
import com.example.springserver.domain.center.entity.Admin;
import com.example.springserver.domain.caregiver.entity.Caregiver;
import com.example.springserver.domain.center.entity.Center;
import com.example.springserver.domain.center.dto.request.AdminRequestDto;
import com.example.springserver.domain.center.repository.AdminRepository;
import com.example.springserver.domain.caregiver.repository.CaregiverRepository;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.GlobalException;
import com.example.springserver.domain.center.repository.CenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JoinService {

    private final CaregiverRepository caregiverRepository;
    private final CenterRepository centerRepository;
    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CertificateRepository certificateRepository;
    private final ExperienceRepository experienceRepository;
    private final S3Service s3Service;

    @Transactional
    public Caregiver signUpCaregiver(CaregiverSignupRequest request, MultipartFile profileImg) {

        List<CertificateRequest> certificateRequestList = request.getCertificateRequestList();
        List<ExperienceRequest> experienceRequestList = request.getExperienceRequestList();

        // 요청 객체 검증
        validAdmin(request.getBasicInfo().getUsername());
        validCareGiver(request.getBasicInfo().getUsername());

        // 이미지 적용
        String imgUrl = saveImgUrl(profileImg);
        Caregiver saved = caregiverRepository.save(CaregiverConverter.toCaregiver(request, bCryptPasswordEncoder, imgUrl));

        //자격증저장
        if (certificateRequestList !=null)
            for(CertificateRequest dto : certificateRequestList)
                certificateRepository.save(CaregiverConverter.toCertificate(saved,dto));

        //경력저장
        if (experienceRequestList !=null)
            for (ExperienceRequest dto : experienceRequestList)
                experienceRepository.save(CaregiverConverter.toExperience(saved,dto));

        return saved;
    }

    @Transactional
    public Admin signUpAdmin(AdminRequestDto.SignUpAdminReq request) {

        // 요청 객체 검증
        validAdmin(request.getUsername());
        validCareGiver(request.getUsername());
        Center centerData = validCenter(request.getCenterName());

        // Admin 객체 converter를 통해 생성
        if(!centerData.getCertification().equals(request.getCenterCertification()))
            throw new GlobalException(ErrorCode.CENTER_CERTIFICATION_FAIL);

        Admin newAdmin = AdminConverter.toAdmin(request, bCryptPasswordEncoder, centerData);
        // 양방향 연관관계 매핑
        newAdmin.changeCenter(centerData);

        return adminRepository.save(newAdmin);
    }

    private void validAdmin(String adminName) {
        if(adminRepository.existsByUsername(adminName))
            throw new GlobalException(ErrorCode.MEMBER_IS_EXIST);
    }

    private void validCareGiver(String caregiverName) {
        if(caregiverRepository.existsByUsername(caregiverName))
                throw new GlobalException(ErrorCode.CAREGIVER_IS_EXIST);
    }

    private Center validCenter(String centerName) {
        return centerRepository.findByCenterName(centerName)
                .orElseThrow(() -> new GlobalException(ErrorCode.CENTER_NOT_FOUND));
    }

    private String saveImgUrl(MultipartFile profileImg) {
        if(profileImg == null) {
            return "http://localhost:8080/basicImg.jpeg";
        } else {
            return s3Service.uploadFileImage(profileImg);
        }
    }
}
