package com.pi.stepup.domain.music.constant;

public enum MusicApiUrl {
    CREATE_MUSIC_URL("/api/music"),
    READ_ONE_MUSIC_URL("/api/music/"),
    READ_ALL_MUSIC_URL("/api/music?keyword="),
    DELETE_MUSIC_URL("/api/music/")
    ;

    private String url;
    MusicApiUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return this.url;
    }
}
