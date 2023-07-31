package com.pi.stepup.domain.dance.constant;

public enum DanceExceptionMessage {

    //TODO: 개최, 예약, 참여 권한 검증도 service에 추가하기

    DANCE_NOT_FOUND("랜덤 플레이 댄스 조회 실패"),
    DANCE_CREATE_FORBIDDEN("랜덤 플레이 댄스 개최 권한 없음"),
    DANCE_UPDATE_FORBIDDEN("랜덤 플레이 댄스 수정 권한 없음"),
    DANCE_DELETE_FORBIDDEN("랜덤 플레이 댄스 삭제 권한 없음"),
    DANCE_INVALID_TIME("유효하지 않은 시간"),
    DANCE_INVALID_MUSIC("유효하지 않은 노래 개수(10곡 이상 50곡 이하만 가능)"),
    RESERVATION_CREATE_FORBIDDEN("랜덤 플레이 댄스 예약 권한 없음"),
    RESERVATION_DUPLICATED("랜덤 플레이 댄스 예약 중복"),
    RESERVATION_IMPOSSIBLE("랜덤 플레이 댄스 예약 불가(본인이 개최한 랜덤 플래이 댄스)"),
    RESERVATION_DELETE_FORBIDDEN("랜덤 플레이 댄스 예약 취소 권한 없음"),
    ATTEND_CREATE_FORBIDDEN("랜덤 플레이 댄스 참여 권한 없음"),
    ATTEND_DUPLICATED("랜덤 플레이 댄스 참여 중복(이미 DB에 삽입된 데이터)");

    private final String message;

    DanceExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}
