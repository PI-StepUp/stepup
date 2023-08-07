package com.pi.stepup.domain.dance.constant;

public enum DanceUrl {

    CREATE_RANDOM_DANCE_URL(""),
    UPDATE_OPEN_RANDOM_DANCE_URL("/my"),
    DELETE_OPEN_RANDOM_DANCE_URL("/my/"),
    SELECT_ALL_OPEN_RANDOM_DANCE_URL("/my/open"),
    SELECT_ALL_DANCE_MUSIC_URL("/playlist/"),
    SELECT_ALL_RANDOM_DANCE_URL("?progressType=ALL&keyword="),
    SELECT_IN_PROGRESS_RANDOM_DANCE_URL("?progressType=SCHEDULED&keyword="),
    SELECT_SCHEDULED_RANDOM_DANCE_URL("?progressType=IN_PROGRESS&keyword="),
    RESERVE_RANDOM_DANCE_URL("/reserve/"),
    DELETE_RESERVE_RANDOM_DANCE_URL("/my/reserve/"),
    SELECT_ALL_RESERVE_RANDOM_DANCE_URL("/my/reserve"),
    ATTEND_RANDOM_DANCE_URL("/attend/"),
    SELECT_ALL_ATTEND_RANDOM_DANCE_URL("/my/attend");

    private final String url;

    DanceUrl(String url) {
        String dance = "/api/dance";
        this.url = dance + url;
    }

    public String getUrl() {
        return this.url;
    }

}
