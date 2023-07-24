package com.pi.stepup.domain.board.dto;

import com.pi.stepup.domain.board.domain.Comment;
import com.pi.stepup.domain.board.domain.Talk;
import com.pi.stepup.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class TalkResponseDto {

    private final Long boardId;
    private final User writer;
    private final String title;
    private final String content;
    private final List<Comment> comments;
    private final String fileURL;
    private int commentCnt;


    @Builder
    public TalkResponseDto(Talk talk) {
        this.boardId = talk.getBoardId();
        this.writer = talk.getWriter();
        this.title = talk.getTitle();
        this.content = talk.getContent();
        this.fileURL = talk.getFileURL();
        this.comments = talk.getComments();
        this.commentCnt = talk.getCommentCnt();
    }
}
