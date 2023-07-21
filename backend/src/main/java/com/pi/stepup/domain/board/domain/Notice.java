package com.pi.stepup.domain.board.domain;

import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.user.domain.User;
import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
    public Notice(Long boardId, User writer, String title, String content, List<Comment> comments, String fileURL, RandomDance randomDance) {
        super(boardId, writer, title, content, comments, fileURL);
        this.randomDance = randomDance;
    }

}
