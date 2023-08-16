package com.pi.stepup.domain.board.domain;

import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("NOTICE")
public class Notice extends Board {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RANDOM_DANCE_ID")
    private RandomDance randomDance;

    @Builder
    public Notice(Long boardId, User writer, String title, String content, List<Comment> comments, String fileURL, String boardType, Long viewCnt, RandomDance randomDance) {
        super(boardId, writer, title, content, comments, fileURL, boardType, viewCnt);
        this.randomDance = randomDance;
    }

    public void update(String title, String content, String fileURL, RandomDance randomDance) {
        this.title = title;
        this.content = content;
        this.fileURL = fileURL;
        this.randomDance = randomDance;
    }
}
