package com.pi.stepup.domain.user.constant;

public enum UserResponseMessage {
    READ_ALL_COUNTRIES_SUCCESS("국가 코드 목록 조회 완료"),
    CHECK_EMAIL_DUPLICATED_SUCCESS("이메일 사용 가능"),
    CHECK_EMAIL_DUPLICATED_FAIL("이메일 중복");

    private final String message;

    UserResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
