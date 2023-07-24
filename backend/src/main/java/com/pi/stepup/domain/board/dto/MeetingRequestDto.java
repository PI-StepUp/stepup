package com.pi.stepup.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class MeetingRequestDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MeetingSaveRequestDto {

        private String id;
        private String title;
        private String content;
        private String fileURL;
        private LocalDateTime startAt;
        private LocalDateTime endAt;
        private String region;
        private int commentCnt;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MeetingFindOneRequestDto {
        private Long boardId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MeetingFindByKeywordRequestDto {
        private String keyword;
    }

}
