package com.pi.stepup.domain.board.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.global.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    Long boardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WRITER")
    User writer;

    @Column(nullable = false)
    String title;

    @Column(columnDefinition = "TEXT")
    String content;

    //댓글 정렬 최신순
    @OrderBy("commentId desc")
    @JsonIgnoreProperties({"board"})
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Comment> comments = new ArrayList<>();

    @Column(name = "FILE_URL")
    String fileURL;

    @Column(name = "BOARD_TYPE", insertable = false, updatable = false)
    String boardType;

    @Column(name = "VIEW_CNT", nullable = false)
    Long viewCnt;

    public void increaseViewCnt() {
        this.viewCnt++;
    }
}
