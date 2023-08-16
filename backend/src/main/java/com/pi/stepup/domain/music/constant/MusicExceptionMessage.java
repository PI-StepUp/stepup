package com.pi.stepup.domain.music.constant;

public enum MusicExceptionMessage {
    MUSIC_NOT_FOUND("노래 조회 실패"),
    MUSIC_ANSWER_NOT_FOUND("노래 정답 조회 실패"),
    MUSIC_DUPLICATED("노래 중복"),
    MUSIC_DELETE_FAIL("노래 삭제 실패"),
    MUSIC_APPLY_NOT_FOUND("노래 신청 조회 실패"),
    MUSIC_APPLY_DELETE_FAIL("노래 신청 삭제 실패"),
    UNAUTHORIZED_USER_ACCESS("접근 권한 없음"),
    ADD_HEART_FAIL("좋아요 실패"),
    REMOVE_HEART_FAIL("좋아요 취소 실패")
    ;


    private final String message;

    MusicExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
