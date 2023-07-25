package com.pi.stepup.domain.user.constant;

public enum UserExceptionMessage {
    EMAIL_DUPLICATED("이메일 중복"),
    NICKNAME_DUPLICATED("닉네임 중복"),
    ID_DUPLICATED("아이디 중복"),
    USER_NOT_FOUND("회원 정보 조회 실패");

    private final String message;

    UserExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
