package com.pi.stepup.domain.board.domain;

import com.pi.stepup.domain.user.domain.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("MEETING")
public class Meeting extends Board {
    private String region;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private int commentCnt;

    @Builder
    public Meeting(Long boardId, User writer, String title, String content, List<Comment> comments, String fileURL, String region, LocalDateTime startAt, LocalDateTime endAt, int commentCnt) {
        super(boardId, writer, title, content, comments, fileURL);
        this.region = region;
        this.startAt = startAt;
        this.endAt = endAt;
        this.commentCnt = commentCnt;
    }

}
