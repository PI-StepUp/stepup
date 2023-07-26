package com.pi.stepup.domain.board.dto.meeting;

import com.pi.stepup.domain.board.domain.Comment;
import com.pi.stepup.domain.board.domain.Meeting;
import com.pi.stepup.domain.board.domain.Notice;
import com.pi.stepup.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class MeetingResponseDto {

    @Getter
    public static class MeetingInfoResponseDto {
        private final Long boardId;
        private final String title;
        private final String content;
        private final String writerName;
        private final String profileImg;
        private final String fileURL;
        private final String boardType;
        private final String region;
        private final LocalDateTime startAt;
        private final LocalDateTime endAt;
        private final List<Comment> comments;
        private int commentCnt;

        @Builder
        public MeetingInfoResponseDto(Meeting meeting) {
            this.boardId = meeting.getBoardId();
            this.title = meeting.getTitle();
            this.content = meeting.getContent();
            this.writerName = meeting.getWriter().getNickname();
            this.profileImg = meeting.getWriter().getProfileImg();
            this.fileURL = meeting.getFileURL();
            this.boardType = meeting.getBoardType();
            this.region = meeting.getRegion();
            this.startAt = meeting.getStartAt();
            this.endAt = meeting.getEndAt();
            this.comments = meeting.getComments();
            this.commentCnt = meeting.getCommentCnt();
        }
    }
}
