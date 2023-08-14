package com.pi.stepup.domain.board.constant;

public enum BoardExceptionMessage {
    BOARD_NOT_FOUND("게시글 조회 실패"),
    COMMENT_NOT_FOUND("댓글 조회 실패"),
    MEETING_INVALID_TIME("유효하지 않은 시간");

    private final String message;

    BoardExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
