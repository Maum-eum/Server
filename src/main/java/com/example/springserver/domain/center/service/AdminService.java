package com.example.springserver.domain.center.service;

import com.example.springserver.domain.caregiver.repository.CaregiverRepository;
import com.example.springserver.domain.center.converter.AdminConverter;
import com.example.springserver.domain.center.dto.request.AdminRequestDTO;
import com.example.springserver.domain.center.dto.response.AdminResponseDTO;
import com.example.springserver.domain.center.entity.Admin;
import com.example.springserver.domain.center.entity.Center;
import com.example.springserver.domain.center.repository.AdminRepository;
import com.example.springserver.domain.center.repository.CenterRepository;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.GlobalException;
import com.example.springserver.global.security.util.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final CaregiverRepository caregiverRepository;
    private final CenterRepository centerRepository;

    public AdminResponseDTO.SearchAdminResult searchAdmin(CustomUserDetails admin) {
        String adminUsername = admin.getUsername();
        Admin adminData = adminRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new GlobalException(ErrorCode.ADMIN_NOT_FOUND));

        Center center = centerRepository.findByCenterLeaderName(adminUsername);
        Boolean isLeader = Objects.equals(center, adminData.getCenter());

        return AdminConverter.toSearchAdminResult(adminData, isLeader);
    }

    public AdminResponseDTO.SearchAdminResult updateAdmin(CustomUserDetails admin, AdminRequestDTO.UpdateAdminReq request) {
        String adminUsername = admin.getUsername();
        Admin adminData = adminRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new GlobalException(ErrorCode.ADMIN_NOT_FOUND));

        if (request.getName() != null) {
            adminData.setName(request.getName());
        }
        if (request.getConnect() != null) {
            adminData.setConnect(request.getConnect());
        }

        adminRepository.save(adminData);

        Center center = centerRepository.findByCenterLeaderName(adminUsername);
        Boolean isLeader = Objects.equals(center, adminData.getCenter());

        return AdminConverter.toSearchAdminResult(adminData, isLeader);
    }

    public AdminResponseDTO.DeleteAdminResult deleteAdmin(CustomUserDetails admin) {
        String adminUsername = admin.getUsername();
        Admin adminData = adminRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new GlobalException(ErrorCode.ADMIN_NOT_FOUND));

        // todo: 삭제하는 관리자가 센터장일 경우 처리 필요
        try {
            adminRepository.delete(adminData);
            return AdminConverter.toDeleteAdminResult(true);
        } catch (Exception e) {
            throw new GlobalException(ErrorCode.ADMIN_DELETE_FAILED);
        }
    }
}
