package com.pi.stepup.domain.music.constant;

public enum MusicResponseMessage {
    CREATE_MUSIC_SUCCESS("노래 등록 완료"),
    READ_ONE_MUSIC_SUCCESS("노래 조회 완료"),
    READ_ALL_MUSIC_SUCCESS("노래 목록 조회 완료"),
    DELETE_MUSIC_SUCCESS("노래 삭제 완료");

    private final String message;

    MusicResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
