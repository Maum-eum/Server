package com.example.springserver.global.security.jwt;

import com.example.springserver.domain.center.entity.Admin;
import com.example.springserver.domain.center.repository.AdminRepository;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.GlobalException;
import com.example.springserver.global.apiPayload.format.ResultResponse;
import com.example.springserver.global.common.dto.request.UserRequestDTO;
import com.example.springserver.global.security.util.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final AdminRepository adminRepository;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, AdminRepository adminRepository) {

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.adminRepository = adminRepository;
        setFilterProcessesUrl("/login"); // 원하는 엔드포인트로 변경
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        try {
            // JSON 바디에서 username과 password 추출
            ObjectMapper objectMapper = new ObjectMapper();
            UserRequestDTO.LoginDTO loginRequest = null;
            loginRequest = objectMapper.readValue(req.getInputStream(), UserRequestDTO.LoginDTO.class);

            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();

            // 스프링 시큐리티에서 username과 password를 검증하기 위해 token에 담는다.
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

            // token에 담은 데이터를 검증을 위한 AuthenticationManager로 전달
            // 이 메서드는 CustomUserDetailsService를 통해 사용자의 정보를 조회하고 검증합니다.
            // CustomUserDetailsService와 CustomUserDetails를 통해 DB내의 사용자 정보를 조회하고 입력받은 authToken과 비교를 한다.
            // 인증이 성공적으로 이루어지면, CustomUserDetails를 통해 사용자 정보와 권한을 제공한다.
            return authenticationManager.authenticate(authToken);

        } catch (IOException e) {
            throw new AuthenticationException("입력 형식이 잘못됐습니다.", e) {};
        }
    }

    // 로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    // Authentication(getPrincipal(), getCredentials(), getAuthorities(), isAuthenticated(), getDetails(), getName())
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        // 유저 정보
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId(); // 사용자 ID 가져오기
        String username = userDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        // 토큰 생성
        String access = jwtUtil.createJwt("access", role, username, userId, 86400000L);

        // 응답 데이터 구성
        Map<String, Object> responseData = Map.of(
                "userId", userId,
                "role", role
        );

        // ROLE_ADMIN이면 centerId 추가
        if ("ROLE_ADMIN".equals(role)) {
            Admin admin = adminRepository.findByUsername(username)
                    .orElseThrow(() -> new GlobalException(ErrorCode.ADMIN_NOT_FOUND));
            Long centerId = admin.getCenter().getCenterId();
            String centerName = admin.getCenter().getCenterName();
            String name = admin.getName();
            responseData = new HashMap<>(responseData); // 불변 Map을 변경 가능하도록 변환
            responseData.put("centerId", centerId);
            responseData.put("centerName", centerName);
            responseData.put("name", name);
        }

        ResultResponse<Map<String, Object>> responseBody = ResultResponse.success(responseData);

        // 응답 설정
        response.setHeader("Authorization", "Bearer " + access);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {

        ResultResponse<String> responseBody = ResultResponse.fail("아이디 또는 비밀번호가 올바르지 않습니다.");

        // 응답 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }
}
