package com.pi.stepup.domain.board.dto.meeting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class MeetingRequestDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MeetingSaveRequestDto {
        @NotBlank
        private String id;
        @NotBlank
        private String title;
        @NotBlank
        private String content;
        private String fileURL;
        @NotEmpty
        private LocalDateTime startAt;
        @NotEmpty
        private LocalDateTime endAt;
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
        @NotEmpty
        private LocalDateTime startAt;
        @NotEmpty
        private LocalDateTime endAt;
    }
}
