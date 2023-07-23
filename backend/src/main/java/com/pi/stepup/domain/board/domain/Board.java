package com.pi.stepup.domain.board.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.global.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import lombok.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "BOARD_TYPE")
public abstract class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long boardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WRITER")
    private User writer;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    //댓글 정렬 최신순
    @OrderBy("commentId desc")
    @JsonIgnoreProperties({"board"})
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Column(name = "FILE_URL")
    private String fileURL;

    @Builder
    public Board(String title, String content, List<Comment> comments, String fileURL) {
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.comments = comments;
        this.fileURL = fileURL;
    }

    public void update(String title, String content, String fileURL) {
        this.title = title;
        this.content = content;
        this.fileURL = fileURL;
    }
}
