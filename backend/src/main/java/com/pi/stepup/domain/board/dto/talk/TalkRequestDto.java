package com.pi.stepup.domain.board.dto.talk;

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
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TalkUpdateRequestDto {
        private Long boardId;
        private String title;
        private String content;
        private String writerName;
        private String writerProfileImg;
        private String fileURL;
        private String boardType;
    }
}
