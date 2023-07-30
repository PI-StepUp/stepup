package com.pi.stepup.domain.dance.constant;

public enum DanceExceptionMessage {


    DANCE_NOT_FOUND("랜덤 플레이 댄스 조회 실패"),
    DANCE_UPDATE_FORBIDDEN("랜덤 플레이 댄스 수정 권한 없음"),
    DANCE_DELETE_FORBIDDEN("랜덤 플레이 댄스 수정 권한 없음"),
    DANCEMUSICLIST_IS_NEEDED("노래 목록 등록 요청"),
    DANCEMUSICLIST_NOT_FOUND("노래 목록 조회 실패"),
    RESERVATION_DUPLICATED("예약 중복"),
    RESERVATION_IMPOSSIBLE("예약 불가(본인이 개최한 랜덤 플래이 댄스)"),
    RESERVATION_NOT_FOUND("랜덤 플레이 댄스 예약 조회 실패"),
    ATTEND_DUPLICATED("참여 중복"),
    ATTEND_NOT_FOUND("랜덤 플레이 댄스 예약 조회 실패");

    //예약 취소 관련 검증은...
    //이미 로그인한 아이디 정보로 예약 정보 가져와서...

    private final String message;

    DanceExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}
