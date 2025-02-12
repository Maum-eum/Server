package com.example.springserver.service;
import com.example.springserver.domain.center.converter.AdminConverter;
import com.example.springserver.domain.caregiver.converter.CaregiverConverter;
import com.example.springserver.domain.center.entity.Admin;
import com.example.springserver.domain.caregiver.entity.Caregiver;
import com.example.springserver.domain.center.entity.Center;
import com.example.springserver.domain.center.dto.request.AdminRequestDTO;
import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDTO;
import com.example.springserver.domain.center.repository.AdminRepository;
import com.example.springserver.domain.caregiver.repository.CaregiverRepository;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.GlobalException;
import com.example.springserver.repository.center.CenterRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    private final CaregiverRepository caregiverRepository;
    private final CenterRepository centerRepository;
    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(CaregiverRepository caregiverRepository, CenterRepository centerRepository, AdminRepository adminRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.caregiverRepository = caregiverRepository;
        this.centerRepository = centerRepository;
        this.adminRepository = adminRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public Caregiver signUpCaregiver(CaregiverRequestDTO.SignUpCaregiverReq request) {

        Boolean isExist = caregiverRepository.existsByUsername(request.getUsername());

        if(isExist){
            throw new GlobalException(ErrorCode.MEMBER_IS_EXIST);
        }

        // Caregiver 객체 converter를 통해 생성
        Caregiver newCaregiver = CaregiverConverter.toCaregiver(request, bCryptPasswordEncoder);

        return caregiverRepository.save(newCaregiver);
    }

    public Admin signUpAdmin(AdminRequestDTO.SignUpAdminReq request) {

        Boolean isExist = adminRepository.existsByUsername(request.getUsername());

        if(isExist){
            throw new GlobalException(ErrorCode.MEMBER_IS_EXIST);
        }

        Center centerData = centerRepository.findByCenterName(request.getCenterName())
                .orElseThrow(() -> new GlobalException(ErrorCode.CENTER_NOT_FOUND));

        // Admin 객체 converter를 통해 생성
        Admin newAdmin = AdminConverter.toAdmin(request, bCryptPasswordEncoder, centerData);

        return adminRepository.save(newAdmin);
    }

}
