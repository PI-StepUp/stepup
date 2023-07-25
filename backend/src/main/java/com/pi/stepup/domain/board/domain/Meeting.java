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
    public Meeting(Long boardId, User writer, String title, String content, List<Comment> comments, String fileURL, String boardType, String region, LocalDateTime startAt, LocalDateTime endAt, int commentCnt) {
        super(boardId, writer, title, content, comments, fileURL, boardType);
        this.region = region;
        this.startAt = startAt;
        this.endAt = endAt;
        this.commentCnt = commentCnt;
    }

    public void updateCommentCnt() {
        if (getComments() != null) {
            this.commentCnt = getComments().size();
        } else {
            this.commentCnt = 0;
        }
    }

    // 댓글이 추가될 때 호출
    @PostPersist
    public void updateCommentCntOnCommentAdd() {
        updateCommentCnt();
    }

    // 댓글이 삭제될 때 호출
    @PostRemove
    public void updateCommentCntOnCommentRemove() {
        updateCommentCnt();
    }
}

