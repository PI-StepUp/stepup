package com.pi.stepup.domain.board.constant;

public enum NoticeApiUrls {
    CREATE_NOTICE_URL(""),
    UPDATE_NOTICE_URL("/"),
    DELETE_NOTICE_URL("/"),
    READ_ONE_NOTICE_URL("/"),
    READ_ALL_NOTICE_URL("?keyword=");

    private final String url;

    NoticeApiUrls(String url) {
        String notice = "/api/board/notice";
        this.url = notice + url;
    }

    public String getUrl() {
        return this.url;
    }
}
