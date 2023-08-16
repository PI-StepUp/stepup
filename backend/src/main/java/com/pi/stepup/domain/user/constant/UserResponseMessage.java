package com.pi.stepup.domain.user.constant;

public enum UserResponseMessage {
    READ_ALL_COUNTRIES_SUCCESS("국가 코드 목록 조회 완료"),
    READ_STATISTICS_OF_USER_COUNTRY_SUCCESS("국가 별 사용자 통계 조회 완료"),
    CHECK_EMAIL_DUPLICATED_SUCCESS("이메일 사용 가능"),
    CHECK_NICKNAME_DUPLICATED_SUCCESS("닉네임 사용 가능"),
    CHECK_ID_DUPLICATED_SUCCESS("아이디 사용 가능"),
    SIGN_UP_SUCCESS("회원가입 완료"),
    LOGIN_SUCCESS("로그인 완료"),
    READ_ONE_SUCCESS("회원정보 조회 완료"),
    DELETE_SUCCESS("회원 탈퇴 완료"),
    CHECK_PASSWORD_SUCCESS("비밀번호 일치"),
    UPDATE_USER_SUCCESS("회원정보 수정 완료"),
    CHANGE_PASSWORD_SUCCESS("비밀번호 변경 완료"),
    FIND_ID_SUCCESS("아이디 전송 완료"),
    FIND_PASSWORD_SUCCESS("임시 비밀번호 전송 완료"),
    REISSUE_TOKENS_SUCCESS("토큰 재발급 완료");

    private final String message;

    UserResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
