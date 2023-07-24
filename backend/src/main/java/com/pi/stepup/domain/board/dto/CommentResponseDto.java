package com.pi.stepup.domain.board.dto;

import com.pi.stepup.domain.board.domain.Comment;
import com.pi.stepup.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;


@Getter
public class CommentResponseDto {

    private Long commentId;
    private Long boardId;
    private User writer;
    private String content;

    @Builder
    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.boardId = comment.getBoard().getBoardId();
        this.writer = comment.getWriter();
        this.content = comment.getContent();
    }
}

