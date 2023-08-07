package com.pi.stepup.domain.board.constant;

public enum CommentApiUrls {
    CREATE_COMMENT_URL(""),
    DELETE_COMMENT_URL("/");

    private final String url;

    CommentApiUrls(String url) {
        String comment = "/api/board/comment";
        this.url = comment + url;
    }

    public String getUrl() {
        return this.url;
    }
}
