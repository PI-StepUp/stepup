package com.pi.stepup.domain.dance.constant;

public enum DanceResponseMessage {

    CREATE_RANDOM_DANCE("랜덤 플레이 댄스 생성 완료"),
    UPDATE_OPEN_RANDOM_DANCE("랜덤 플레이 댄스 수정 완료"),
    DELETE_OPEN_RANDOM_DANCE("랜덤 플레이 댄스 삭제 완료"),
    SELECT_ALL_OPEN_RANDOM_DANCE("내가 개최한 랜덤 플레이 댄스 목록 조회 완료"),
    SELECT_ALL_DANCE_MUSIC("사용 노래 목록 조회 완료"),
    SELECT_ALL_RANDOM_DANCE("참여 가능한 랜덤 플레이 댄스 목록 조회 완료"),
    SELECT_IN_PROGRESS_RANDOM_DANCE("진행 중인 랜덤 플레이 댄스 목록 조회 완료"),
    SELECT_SCHEDULED_RANDOM_DANCE("진행 예정된 랜덤 플레이 댄스 목록 조회 완료"),
    RESERVE_RANDOM_DANCE("랜덤 플레이 댄스 예약 완료"),
    DELETE_RESERVE_RANDOM_DANCE("랜덤 플레이 댄스 예약 취소 완료"),
    SELECT_ALL_RESERVE_RANDOM_DANCE("내가 예약한 랜덤 플레이 댄스 목록 조회 완료"),
    ATTEND_RANDOM_DANCE("랜덤 플레이 댄스 참여 완료"),
    DELETE_ATTEND_RANDOM_DANCE("랜덤 플레이 댄스 참여 취소 완료"),
    SELECT_ALL_ATTEND_RANDOM_DANCE("내가 참여한 랜덤 플레이 댄스 목록 조회 완료");

    private final String message;
    DanceResponseMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
