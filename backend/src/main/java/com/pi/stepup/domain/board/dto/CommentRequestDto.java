package com.pi.stepup.domain.board.dto;

import com.pi.stepup.domain.board.domain.Board;
import com.pi.stepup.domain.board.domain.Comment;
import com.pi.stepup.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public class CommentRequestDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentSaveRequestDto {
        private Long boardId;
        private Long userId;
        private String content;

        public Comment toEntity (Board board, User writer) {
            return Comment.builder()
                    .writer(writer)
                    .board(board)
                    .content(content)
                    .build();
        }
    }
}
