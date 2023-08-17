package com.pi.stepup.domain.board.dto.meeting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class MeetingRequestDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MeetingSaveRequestDto {
        @NotBlank
        private String title;
        @NotBlank
        private String content;
        private String fileURL;
        private String startAt;
        private String endAt;
        @NotBlank
        private String region;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MeetingUpdateRequestDto {
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
        @NotBlank
        private String region;
        private String startAt;
        private String endAt;
    }
}
