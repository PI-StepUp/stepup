package com.pi.stepup.domain.board.domain;

import com.pi.stepup.domain.user.domain.User;
import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("TALK")
public class Talk extends Board {
    private int commentCnt;

    @Builder
    public Talk(Long boardId, User writer, String title, String content, List<Comment> comments, String fileURL, int commentCnt) {
        super(boardId, writer, title, content, comments, fileURL);
        this.commentCnt = commentCnt;
    }

}
