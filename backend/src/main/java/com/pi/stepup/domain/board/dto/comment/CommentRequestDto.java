package com.pi.stepup.domain.board.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CommentRequestDto {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentSaveRequestDto{
        private Long boardId;
        private String id;
        private String content;
    }
}
