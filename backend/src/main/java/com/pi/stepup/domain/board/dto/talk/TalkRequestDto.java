package com.pi.stepup.domain.board.dto.talk;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TalkRequestDto {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TalkSaveRequestDto {
        @NotBlank
        private String title;
        @NotBlank
        private String content;
        private String fileURL;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TalkUpdateRequestDto {
        @NotNull
        private Long boardId;
        @NotBlank
        private String title;
        @NotBlank
        private String content;
        @NotBlank
        private String writerName;
        private String writerProfileImg;
        private String fileURL;
        @NotBlank
        private String boardType;
    }
}
