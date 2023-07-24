package com.pi.stepup.domain.board.dto;

import com.pi.stepup.domain.board.domain.Notice;
import com.pi.stepup.domain.dance.domain.RandomDance;
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
        private Long randomDanceId;
        private String title;
        private String content;
        private String fileURL;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NoticeUpdateRequestDto {
        private RandomDance randomDance;
        private String title;
        private String content;
        private String fileURL;

        public NoticeUpdateRequestDto(Notice notice) {
            if (notice != null) {
                this.randomDance = notice.getRandomDance();
                this.title = notice.getTitle();
                this.content = notice.getContent();
                this.fileURL = notice.getFileURL();
            }
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NoticeFindOneRequestDto {
        private Long boardId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NoticeFindByKeywordRequestDto {
        private String keyword;
    }
}
