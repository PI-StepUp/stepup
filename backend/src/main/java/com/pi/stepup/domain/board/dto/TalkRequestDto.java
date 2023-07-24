package com.pi.stepup.domain.board.dto;

import com.pi.stepup.domain.board.domain.Talk;
import com.pi.stepup.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TalkRequestDto {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TalkSaveRequestDto {
        private String id;
        private String title;
        private String content;
        private String fileURL;
        private int commentCnt;

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
