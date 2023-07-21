package com.pi.stepup.domain.board.domain;

import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.global.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User writer;

    @Builder
    public Comment(String content, Board board, User writer) {
        this.content = content;
        this.board = board;
        this.writer = writer;
    }

}