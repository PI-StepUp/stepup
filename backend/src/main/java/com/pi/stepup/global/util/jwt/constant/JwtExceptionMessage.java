package com.pi.stepup.global.util.jwt.constant;

public enum JwtExceptionMessage {
    EXPIRED_TOKEN("만료된 토큰"),
    INVALID_TOKEN("유효하지 않은 토큰"),
    MALFORMED_HEADER("잘못된 형식의 헤더"),
    NOT_MATCHED_TOKEN("토큰 불일치");

    private final String message;

    JwtExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
