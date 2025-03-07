package com.example.springserver.global.apiPayload.format;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),


    // 멤버 관려 에러
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "사용자가 없습니다."),
    MEMBER_IS_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4002", "사용자가 이미 존재합니다.."),
    NICKNAME_NOT_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4003", "닉네임은 필수 입니다."),
    INVALID_MEMBER_ROLE(HttpStatus.BAD_REQUEST, "MEMBER4004", "사용자의 ROLE이 잘못 되었습니다."),
    USERNAME_IS_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4005", "해당 아이디가 이미 존재합니다."),

    // 토큰 관련 에러
    ACCESS_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "TOKEN4001", "액세스 토큰이 만료되었습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "TOKEN4002", "리프레시 토큰이 만료되었습니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.BAD_REQUEST, "TOKEN4003", "토큰이 올바르지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "TOKEN4004", "토큰이 올바르지 않습니다."),
    REFRESH_TOKEN_NOT_EXIST(HttpStatus.BAD_REQUEST, "TOKEN4005", "리프레시 토큰이 존재하지 않습니다."),
    REFRESH_TOKEN_IS_NULL(HttpStatus.BAD_REQUEST, "TOKEN4006", "리프레시 토큰이 null입니다."),

    // 센터 관련 에러
    CENTER_NOT_FOUND(HttpStatus.BAD_REQUEST, "CENTER4001", "센터가 존재하지 않습니다."),
    CENTER_CERTIFICATION_FAIL(HttpStatus.BAD_REQUEST,"CENTER4002" ,"센터인증에 실패했습니다."),

    // 어르신 관련 에러
    ELDER_NOT_FOUND(HttpStatus.BAD_REQUEST, "ELDER4001", "어르신 정보가 존재하지 않습니다."),
    ELDER_NOT_BELONG_TO_CENTER(HttpStatus.BAD_REQUEST, "ELDER4001", "센터에 어르신이 존재하지 않습니다."),
    INVALID_TEMP_ELDER_DATA(HttpStatus.BAD_REQUEST, "ELDER4003", "임시 저장시 어르신 이름, 성별, 생일은 필수 입력 항목입니다."),
    INVALID_ELDER_DATA(HttpStatus.BAD_REQUEST, "ELDER4003", "저장시 어르신 전체 정보는 필수 입력 항목입니다."),

    // 어르신 필요 케어 에러
    CARE_INFO_NOT_FOUND(HttpStatus.BAD_REQUEST, "CARE4001", "필요 케어 정보가 존재하지 않습니다."),

    // 구인 관련 에러
    RECRUIT_NOT_FOUND(HttpStatus.BAD_REQUEST, "RECRUIT-COND4001", "구인 정보가 존재하지 않습니다."),
    RECRUIT_TIME_INVALID(HttpStatus.BAD_REQUEST, "RECRUIT-TIME4002", "구인 시간이 올바르지 않습니다."),
    RECRUIT_DAILY_LABOR_TIME_INVALID(HttpStatus.BAD_REQUEST, "RECRUIT-DAILY-LABOR-TIME4002", "근로법 위반: 하루 최대 근무 시간(8시간)을 초과할 수 없습니다."),
    RECRUIT_MONTHLY_LABOR_TIME_INVALID(HttpStatus.BAD_REQUEST, "RECRUIT-MONTHLY-LABOR-TIME4002", "근로법 위반: 한달 최대 근무 시간(40시간)을 초과할 수 없습니다."),
    RECRUIT_LABOR_WAGE_INVALID(HttpStatus.BAD_REQUEST, "RECRUIT-LABOR-WAGE4002", "근로법 위반: 최저임금(10,030원) 미만의 급여입니다."),

    // 요양보호사 관련 에러
    JOB_CONDITION_NOT_FOUND(HttpStatus.NOT_FOUND,"JOB_CONDITION_NOTFOUND4001","보호사의 구직정보가 존재하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "CAREGIVER-NOTFOUND4001","보호사정보가 존재하지 않습니다."),

    // 관리자 관련 에러
    ADMIN_DELETE_FAILED(HttpStatus.BAD_REQUEST,"ADMIN_DELETE_FAILED4001","관리자 계정 삭제를 실패했습니다."),
    ADMIN_NOT_FOUND(HttpStatus.NOT_FOUND, "ADMIN-NOTFOUND4001","관리자정보가 존재하지 않습니다."),


    // 시간 관련 에러
    TIME_LIMIT_OUT_OF_RANGE(HttpStatus.BAD_REQUEST, "TIME_LIMIT_OUT4001","시간 범위 초과 입력입니다."),
    WORK_TIME_INVALID(HttpStatus.BAD_REQUEST,"REQUEST_TIME_INVALID4001" , "근무 시작시간보다 끝시간이 더 짧을 수 없습니다." ),

    // 지역 관련 에러
    BAD_REQUEST(HttpStatus.BAD_REQUEST,"LOCATION4001","찾고자 하는 지역 입력이 없습니다."),
    LOCATION_NOT_FOUND(HttpStatus.BAD_REQUEST,"LOCATION4001","지역이 존재하지 않습니다."),

    // 매치 관련 에러
    MATCH_NOT_FOUND(HttpStatus.BAD_REQUEST,"MATCH4001","매칭을 찾을 수 없습니다."),

    // 예시,,,
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "ARTICLE4001", "게시글이 없습니다."),

    // For test
    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "이거는 테스트"),

    // 이미지 에러
    IMAGE_UPLOAD_FAILED(HttpStatus.BAD_REQUEST, "IMAGE4001", "이미지 업로드에 실패했습니다."),

    // 페이징 에러
    PAGE_NOT_EXIST(HttpStatus.BAD_REQUEST, "PAGE001", "페이지가 0 이하입니다."),
    NOT_ALLOW_STRING(HttpStatus.INTERNAL_SERVER_ERROR,"NO STRING" ,"백엔드 담당자가 String으로 반환을 설정했습니다. String 반환은 허용되지 않습니다. 담당자에게 문의하세요!"),

    // 매칭 에러
    ERROR_AT_CALCULATE_LOGIC(HttpStatus.NOT_FOUND,"MATCH001", "점수계산중 오류발생" ), MONEY_NOT_MATCHED(HttpStatus.BAD_REQUEST,"MATCH002" ,"시급이 일치하지 않습니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;
    // global (공통)
}