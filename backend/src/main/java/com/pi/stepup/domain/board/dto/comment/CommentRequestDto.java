package com.pi.stepup.domain.board.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CommentRequestDto {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentSaveRequestDto{
        @NotNull
        private Long boardId;
        @NotBlank
        private String id;
        @NotBlank
        private String content;
    }
}
