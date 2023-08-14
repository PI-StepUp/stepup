package com.pi.stepup.domain.music.constant;

public enum MusicApplyResponseMessage {
    CREATE_MUSIC_APPLY_SUCCESS("노래 신청 완료"),
    READ_ALL_MUSIC_APPLY_SUCCESS("노래 신청 목록 조회 완료"),
    READ_ONE_MUSIC_APPLY_SUCCESS("노래 신청 상세 조회 완료"),
    READ_MY_MUSIC_APPLY_SUCCESS("나의 노래 신청 목록 조회 완료"),
    DELETE_MUSIC_APPLY_SUCCESS("노래 신청 삭제 완료"),
    ADD_MUSIC_APPLY_LIKE_SUCCESS("노래 신청 좋아요 완료"),
    DELETE_MUSIC_APPLY_LIKE_SUCCESS("좋아요 취소 완료"),
    READ_MUSIC_APPLY_HEART_STATUS_SUCCESS("노래 신청 좋아요 상태 확인 완료");

    private final String message;

    MusicApplyResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
