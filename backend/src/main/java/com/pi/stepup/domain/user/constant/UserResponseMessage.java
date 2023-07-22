package com.pi.stepup.domain.user.constant;

public enum UserResponseMessage {
    READ_ALL_COUNTRIES_SUCCESS("국가 코드 목록 조회 완료"),
    CHECK_EMAIL_DUPLICATED_SUCCESS("이메일 사용 가능"),
    CHECK_EMAIL_DUPLICATED_FAIL("이메일 중복"),
    CHECK_NICKNAME_DUPLICATED_SUCCESS("닉네임 사용 가능"),
    CHECK_NICKNAME_DUPLICATED_FAIL("닉네임 중복"),
    CHECK_ID_DUPLICATED_SUCCESS("아이디 사용 가능"),
    CHECK_ID_DUPLICATED_FAIL("아이디 중복");

    private final String message;

    UserResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
