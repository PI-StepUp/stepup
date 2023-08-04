package com.pi.stepup.domain.music.constant;

public enum MusicApplyApiUrl {
    CREATE_MUSIC_APPLY_URL("/api/music/apply"),
    READ_ALL_BY_KEYWORD_MUSIC_APPLY_URL("/api/music/apply?keyword="),
    READ_ALL_BY_ID_MUSIC_APPLY_URL("/api/music/apply/my?keyword="),
    READ_ONE_MUSIC_APPLY("/api/music/apply/detail/"),
    DELETE_MUSIC_APPLY_URL("/api/music/apply/"),
    ADD_MUSIC_APPLY_HEART_URL("/api/music/apply/heart"),
    DELETE_MUSIC_APPLY_HEART_URL("/api/music/apply/heart/"),
    READ_MUSIC_APPLY_HEART_STATUS_URL("/api/music/apply/heart/");

    private String url;
    MusicApplyApiUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return this.url;
    }
}
