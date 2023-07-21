package com.pi.stepup.domain.board.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@DiscriminatorValue("TALK")
public class Talk extends Board {
    private int commentCnt;
    public int getCommentCnt() {
        return commentCnt;
    }
    public void setCommentCnt(int commentCnt) {
        this.commentCnt = commentCnt;
    }
}
