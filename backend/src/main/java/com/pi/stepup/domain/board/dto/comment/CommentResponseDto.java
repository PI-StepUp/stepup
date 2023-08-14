package com.pi.stepup.domain.board.dto.comment;

import com.pi.stepup.domain.board.domain.Comment;
import lombok.Builder;
import lombok.Getter;

public class CommentResponseDto {
    @Getter
    public static class CommentInfoResponseDto {
        private Long commentId;
        private String writerName;
        private String writerProfileImg;
        private String content;

        @Builder
        public CommentInfoResponseDto(Comment comment) {
            this.commentId = comment.getCommentId();
            this.writerName = comment.getWriter().getNickname();
            this.writerProfileImg = comment.getWriter().getProfileImg();
            this.content = comment.getContent();
        }
    }
}
