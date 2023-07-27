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
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("TALK")
public class Talk extends Board {

    @ColumnDefault("0")
    private int commentCnt;

    @Builder
    public Talk(Long boardId, User writer, String title, String content, List<Comment> comments, String fileURL, String boardType, int commentCnt) {
        super(boardId, writer, title, content, comments, fileURL, boardType);
        this.commentCnt = commentCnt;
    }

    public void update(String title, String content, String fileURL) {
        this.title = title;
        this.content = content;
        this.fileURL = fileURL;
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
