package com.example.springserver.controller;

import com.example.springserver.apiPayload.ApiResponse;
import com.example.springserver.converter.UserConverter;
import com.example.springserver.domain.entity.UserEntity;
import com.example.springserver.dto.UserRequestDTO;
import com.example.springserver.dto.UserResponseDTO;
import com.example.springserver.service.JoinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@Tag(name = "유저 API", description = "유저에 대한 API (join)")
@RequestMapping("/auth")
public class UserController {

    private final JoinService joinService;

    public UserController(JoinService joinService) {
        this.joinService = joinService;
    }

    @Operation(summary = "회원가입", description = "Post (username, password, nickname, gender)")
    @PostMapping("/join")
    public ApiResponse<UserResponseDTO.JoinResultDTO> joinProcess(@RequestBody @Valid UserRequestDTO.JoinDTO request){

        UserEntity newUser = joinService.joinProcess(request);
        return ApiResponse.onSuccess(UserConverter.toJoinResultDTO(newUser));
    }
}
