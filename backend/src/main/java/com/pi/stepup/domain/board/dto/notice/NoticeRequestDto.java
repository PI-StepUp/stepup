package com.pi.stepup.domain.board.dto.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class NoticeRequestDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NoticeSaveRequestDto {
        @NotBlank
        private String id;
        @NotBlank
        private String title;
        @NotBlank
        private String content;
        private String fileURL;
        private Long randomDanceId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NoticeUpdateRequestDto {
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
        private Long randomDanceId;
    }
}
