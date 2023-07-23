package com.pi.stepup.domain.board.dto;

import com.pi.stepup.domain.board.domain.Meeting;
import com.pi.stepup.domain.user.domain.User;
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
        private User writer;
        private String title;
        private String content;
        private String fileURL;
        private LocalDateTime startAt;
        private LocalDateTime endAt;
        private String region;
        private int commentCnt;

        public Meeting toEntity(){
            return Meeting.builder()
                    .writer(writer)
                    .title(title)
                    .content(content)
                    .fileURL(fileURL)
                    .startAt(startAt)
                    .endAt(endAt)
                    .region(region)
                    .commentCnt(commentCnt)
                    .build();
        }
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
