package com.pi.stepup.domain.board.dto;

import com.pi.stepup.domain.board.domain.Comment;
import com.pi.stepup.domain.board.domain.Meeting;
import com.pi.stepup.domain.board.domain.Talk;
import com.pi.stepup.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class TalkRequestDto {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TalkSaveRequestDto {
        private User writer;
        private String title;
        private String content;
        private String fileURL;
        private int commentCnt;

        public Talk toEntity(){
            return Talk.builder()
                    .writer(writer)
                    .title(title)
                    .content(content)
                    .fileURL(fileURL)
                    .commentCnt(commentCnt)
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TalkFindOneRequestDto {
        private Long boardId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TalkFindByKeywordRequestDto {
        private String keyword;
    }
}
