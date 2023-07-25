package com.pi.stepup.domain.board.constant;

public enum BoardResponseMessage {
    CREATE_NOTICE("공지사항 등록 완료"),
    UPDATE_NOTICE("공지사항 수정 완료"),
    DELETE_NOTICE("공지사항 삭제 완료"),
    READ_ALL_NOTICE("공지사항 전체 목록 조회 완료"),
    READ_ONE_NOTICE("공지사항 게시글 조회 완료"),

    CREATE_TALK("자유게시판 등록 완료"),
    UPDATE_TALK("자유게시판 수정 완료"),
    DELETE_TALK("자유게시판 삭제 완료"),
    READ_ALL_TALK("자유게시판 목록 조회 완료"),
    READ_ALL_MY_TALK("내가 작성한 자유게시판 목록 조회"),
    READ_ONE_TALK("자유게시판 게시글 조회 완료"),

    CREATE_MEETING("정모 등록 완료"),
    UPDATE_MEETING("정모 수정 완료"),
    DELETE_MEETING("정모 삭제 완료"),
    READ_ALL_MEETING("정모 목록 조회 완료"),
    READ_ALL_MY_MEETING("내가 작성한 정모 목록 조회"),
    READ_ONE_MEETING("점모 게시글 조회 완료"),

    CREATE_COMMENT("댓글 등록 완료"),
    DELETE_COMMENT("댓글 삭제 완료");


    private final String message;

    BoardResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
