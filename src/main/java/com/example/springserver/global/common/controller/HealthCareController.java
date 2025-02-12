package com.example.springserver.global.common.controller;

import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.GlobalException;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class HealthCareController {

    // ✅ 1️⃣ 기본형 반환 (int, double, boolean 등)
    @GetMapping("/primitive")
    public int getPrimitive() {
        return 42; // 자동으로 ResultResponse.success(42)로 감싸져야 함
    }

    // ✅ 2️⃣ 참조형 반환 (String)
    @GetMapping("/string")
    public String getString() {
        return "Spring Boot is running!";
    }

    // ✅ 3️⃣ DTO 객체 반환
    @GetMapping("/dto")
    public SampleDto getDto() {
        return new SampleDto(1, "Test DTO");
    }

    // ✅ 4️⃣ List<T> 반환
    @GetMapping("/list")
    public List<String> getList() {
        return List.of("Apple", "Banana", "Cherry");
    }

    // ✅ 5️⃣ Map<K, V> 반환
    @GetMapping("/map")
    public Map<String, Object> getMap() {
        return Map.of("name", "John", "age", 30, "active", true);
    }

    // ✅ 6️⃣ 예외 발생 테스트 (정의된 예외)
    @GetMapping("/exception")
    public void throwGlobalException() {
        throw new GlobalException(ErrorCode.MEMBER_NOT_FOUND);
    }

    // ✅ 7️⃣ 예기치 않은 예외 테스트 (ArithmeticException)
    @GetMapping("/unexpected-error")
    public void throwUnexpectedError() {
        int result = 10 / 0; // ArithmeticException 발생
    }

    // ✅ 8️⃣ ResponseEntity<T> 직접 반환 (ResponseBodyAdvice가 적용되지 않아야 함)
    @GetMapping("/response-entity")
    public ResponseEntity<SampleDto> getResponseEntity() {
        return ResponseEntity.ok(new SampleDto(2, "ResponseEntity 사용"));
    }

    // ✅ 9️⃣ POST 요청으로 DTO 테스트
    @PostMapping("/post-dto")
    public SampleDto postDto(@RequestBody SampleDto dto) {
        return dto; // 공통 응답으로 감싸져야 함
    }
}

// ✅ DTO 클래스
@Data
@AllArgsConstructor
class SampleDto {
    private int id;
    private String name;
}

