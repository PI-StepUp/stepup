package com.pi.stepup.domain.board.dto.talk;

import com.pi.stepup.domain.board.domain.Talk;
import com.pi.stepup.domain.board.dto.comment.CommentResponseDto.CommentInfoResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class TalkResponseDto {
    @Getter
    public static class TalkInfoResponseDto {
        private final Long boardId;
        private final String title;
        private final String content;
        private final String writerName;
        private final String profileImg;
        private final String fileURL;
        private final String boardType;
        private final LocalDateTime createdAt;
        private final int commentCnt;
        private final Long viewCnt;
        private final List<CommentInfoResponseDto> comments;

        @Builder
        public TalkInfoResponseDto(Talk talk, List<CommentInfoResponseDto> comments, Long viewCnt) {
            this.boardId = talk.getBoardId();
            this.title = talk.getTitle();
            this.content = talk.getContent();
            this.writerName = talk.getWriter().getNickname();
            this.profileImg = talk.getWriter().getProfileImg();
            this.fileURL = talk.getFileURL();
            this.boardType = talk.getBoardType();
            this.createdAt = talk.getCreatedAt();
            this.commentCnt = talk.getCommentCnt();
            this.viewCnt = viewCnt;
            this.comments = comments;
        }
    }
}
