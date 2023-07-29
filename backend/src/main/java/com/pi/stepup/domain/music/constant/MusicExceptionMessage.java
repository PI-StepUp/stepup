package com.pi.stepup.domain.music.constant;

public enum MusicExceptionMessage {
    MUSIC_NOT_FOUND("노래 조회 실패"),
    MUSIC_DUPLICATED("노래 중복"),
    MUSIC_APPLY_NOT_FOUND("노래 신청 조회 실패")
    ;


    private final String message;

    MusicExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
