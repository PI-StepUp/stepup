package com.pi.stepup.domain.board.dto;

import com.pi.stepup.domain.board.domain.Notice;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.user.domain.User;
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
        private User writer;
        private RandomDance randomDance;
        private String title;
        private String content;
        private String fileURL;

        public Notice toEntity(){
            return Notice.builder()
                    .writer(writer)
                    .randomDance(randomDance)
                    .title(title)
                    .content(content)
                    .fileURL(fileURL)
                    .build();
        }
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
