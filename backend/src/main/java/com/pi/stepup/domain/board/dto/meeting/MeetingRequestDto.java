package com.pi.stepup.domain.board.dto.meeting;

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
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MeetingUpdateRequestDto {
        private Long boardId;
        private String title;
        private String content;
        private String writerName;
        private String writerProfileImg;
        private String fileURL;
        private String boardType;
        private String region;
        private LocalDateTime startAt;
        private LocalDateTime endAt;
    }
}
