package com.example.springserver.domain.center.controller;

import com.example.springserver.domain.center.converter.AdminConverter;
import com.example.springserver.domain.center.entity.Admin;
import com.example.springserver.domain.center.dto.request.AdminRequestDTO;
import com.example.springserver.domain.center.dto.response.AdminResponseDTO;
import com.example.springserver.domain.center.service.AdminService;
import com.example.springserver.global.security.util.CustomUserDetails;
import com.example.springserver.service.JoinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@Tag(name = "Admin API", description = "Admin에 해당하는 센터장, 사회복지사에 대한 API")
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {


    private final JoinService joinService;
    private final AdminService adminService;

    @Operation(summary = "회원가입", description = "Post")
    @PostMapping("/signup")
    public AdminResponseDTO.SignUpAdminResult signUpAdmin(@RequestBody @Valid AdminRequestDTO.SignUpAdminReq request){

        Admin newAdmin = joinService.signUpAdmin(request);

        return AdminConverter.toSignUpAdminResult(newAdmin);
    }

    @Operation(summary = "관리자 정보 조회", description = "Get")
    @GetMapping("/profile")
    public AdminResponseDTO.SearchAdminResult getProfileAdmin(@AuthenticationPrincipal CustomUserDetails admin){
        return adminService.searchAdmin(admin);
    }

    @Operation(summary = "관리자 정보 수정", description = "Put")
    @PutMapping("/profile")
    public AdminResponseDTO.SearchAdminResult updateAdmin(@AuthenticationPrincipal CustomUserDetails admin, @RequestBody @Valid AdminRequestDTO.UpdateAdminReq request){
        return adminService.updateAdmin(admin, request);
    }

    @Operation(summary = "관리자 계정 삭제", description = "Delete")
    @DeleteMapping("/")
    public AdminResponseDTO.DeleteAdminResult deleteAdmin(@AuthenticationPrincipal CustomUserDetails admin){
        return adminService.deleteAdmin(admin);
    }
}
