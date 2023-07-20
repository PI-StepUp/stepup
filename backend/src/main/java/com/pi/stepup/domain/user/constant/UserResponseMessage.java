package com.pi.stepup.domain.user.constant;

public enum UserResponseMessage {
    READ_ALL_COUNTRIES_SUCCESS("국가 코드 목록 조회 완료");

    private final String message;

    UserResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
