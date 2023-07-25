package com.pi.stepup.domain.board.dto.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class NoticeRequestDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NoticeSaveRequestDto {
        private String id;
        private String title;
        private String content;
        private String fileURL;
        private Long randomDanceId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NoticeUpdateRequestDto {
        private Long boardId;
        private String title;
        private String content;
        private String writerName;
        private String writerProfileImg;
        private String fileURL;
        private String boardType;
        private Long randomDanceId;
    }
}
