package com.pi.stepup.domain.dance.constant;

public enum DanceResponseMessage {

    CREATE_RANDOM_DANCE("랜덤 플레이 댄스 생성 완료"),
    UPDATE_CREATED_RANDOM_DANCE("랜덤 플레이 댄스 수정 완료"),
    DELETE_CREATED_RANDOM_DANCE("랜덤 플레이 댄스 삭제 완료"),
    SELECT_ALL_MUSIC("노래 목록 조회 완료"),
    SELECT_ALL_RANDOM_DANCE("랜덤 플레이 댄스 목록 조회 완료"),
    SELECT_CREATED_RANDOM_DANCE("내가 개최한 랜덤 플레이 댄스 목록 조회 완료"),
    JOINED_RANDOM_DANCE("랜덤 플레이 댄스 예약"),
    SELECT_JOINED_RANDOM_DANCE("내가 예약한 랜덤 플레이 댄스 목록 조회 완료"),
    DELETE_JOINED_RANDOM_DANCE("랜덤 플레이 댄스 예약 취소");

    private final String message;

    DanceResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
