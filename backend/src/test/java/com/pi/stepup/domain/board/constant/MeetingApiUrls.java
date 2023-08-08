package com.pi.stepup.domain.board.constant;

public enum MeetingApiUrls {
    CREATE_MEETING_URL(""),
    UPDATE_MEETING_URL("/"),
    DELETE_MEETING_URL("/"),
    READ_ONE_MEETING_URL("/"),
    READ_ALL_MEETING_URL("?keyword="),
    READ_ALL_MY_MEETING_URL("/my");


    private final String url;

    MeetingApiUrls(String url) {
        String meeting = "/api/board/meeting";
        this.url = meeting + url;
    }

    public String getUrl() {
        return this.url;
    }
}
