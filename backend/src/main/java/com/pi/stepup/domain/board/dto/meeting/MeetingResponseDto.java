package com.pi.stepup.domain.board.dto.meeting;

import com.pi.stepup.domain.board.domain.Comment;
import com.pi.stepup.domain.board.domain.Meeting;
import com.pi.stepup.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class MeetingResponseDto {

    private final Long boardId;
    private final User writer;
    private final String title;
    private final String content;
    private final List<Comment> comments;
    private final String fileURL;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;
    private final String region;
    private int commentCnt;


    @Builder
    public MeetingResponseDto(Meeting meeting) {
        this.boardId = meeting.getBoardId();
        this.writer = meeting.getWriter();
        this.title = meeting.getTitle();
        this.content = meeting.getContent();
        this.fileURL = meeting.getFileURL();
        this.startAt = meeting.getStartAt();
        this.endAt = meeting.getEndAt();
        this.region = meeting.getRegion();
        this.comments = meeting.getComments();
        this.commentCnt = meeting.getCommentCnt();
    }
}
