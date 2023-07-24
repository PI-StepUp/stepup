package com.pi.stepup.domain.board.dto;

import com.pi.stepup.domain.board.domain.Comment;
import com.pi.stepup.domain.board.domain.Notice;
import com.pi.stepup.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class NoticeResponseDto {

    private final Long boardId;
    private final User writer;
    private final Long randomDanceId;
    private final String title;
    private final String content;
    private final List<Comment> comments;
    private final String fileURL;

    @Builder
    public NoticeResponseDto(Notice notice) {
        this.boardId = notice.getBoardId();
        this.writer = notice.getWriter();
        this.randomDanceId = notice.getRandomDance().getRandomDanceId();
        this.title = notice.getTitle();
        this.content = notice.getContent();
        this.fileURL = notice.getFileURL();
        this.comments = notice.getComments();
    }
}
