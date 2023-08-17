package com.pi.stepup.domain.board.domain;

import com.pi.stepup.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("MEETING")
public class Meeting extends Board {
    private String region;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    @ColumnDefault("0")
    private int commentCnt;

    @Builder
    public Meeting(Long boardId, User writer, String title, String content, List<Comment> comments, String fileURL, String boardType, String region, LocalDateTime startAt, LocalDateTime endAt, int commentCnt, Long viewCnt) {
        super(boardId, writer, title, content, comments, fileURL, boardType, viewCnt);
        this.region = region;
        this.startAt = startAt;
        this.endAt = endAt;
        this.commentCnt = commentCnt;
    }

    public void update(String title, String content, String fileURL, String region, LocalDateTime startAt, LocalDateTime endAt) {
        this.title = title;
        this.content = content;
        this.fileURL = fileURL;
        this.region = region;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public void updateCommentCnt() {
        if (getComments() != null) {
            this.commentCnt = getComments().size();
        } else {
            this.commentCnt = 0;
        }
    }

    @PostPersist
    public void updateCommentCntOnCommentAdd() {
        updateCommentCnt();
    }

    @PostRemove
    public void updateCommentCntOnCommentRemove() {
        updateCommentCnt();
    }
}

