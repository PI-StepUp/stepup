package com.pi.stepup.domain.dance.constant;

public enum DanceExceptionMessage {


    DANCE_NOT_FOUND("랜덤 플레이 댄스 조회 실패"),
    DANCE_UPDATE_FORBIDDEN("랜덤 플레이 댄스 수정 권한 없음"),
    DANCE_DELETE_FORBIDDEN("랜덤 플레이 댄스 삭제 권한 없음"),
    DANCE_INVALID_TIME("유효하지 않은 시간"),
    DANCE_INVALID_MUSIC("유효하지 않은 노래 수"),
    RESERVATION_DUPLICATED("랜덤 플레이 댄스 예약 중복"),
    RESERVATION_IMPOSSIBLE("랜덤 플레이 댄스 예약 불가(본인이 개최한 랜덤 플래이 댄스)"),
    RESERVATION_DELETE_FORBIDDEN("랜덤 플레이 댄스 예약 취소 권한 없음"),
    ATTEND_DUPLICATED("랜덤 플레이 댄스 참여 중복(이미 DB에 삽입된 데이터)");

    private final String message;

    DanceExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}
