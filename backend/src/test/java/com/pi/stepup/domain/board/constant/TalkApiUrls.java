package com.pi.stepup.domain.board.constant;

public enum TalkApiUrls {
    CREATE_TALK_URL(""),
    UPDATE_TALK_URL(""),
    DELETE_TALK_URL("/"),
    READ_ONE_TALK_URL("/"),
    READ_ALL_TALK_URL("?keyword="),
    READ_ALL_MY_TALK_URL("/my");

    private final String url;

    TalkApiUrls(String url) {
        String talk = "/api/board/talk";
        this.url = talk + url;
    }

    public String getUrl() {
        return this.url;
    }
}
