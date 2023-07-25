package com.pi.stepup.domain.music.domain;

import com.pi.stepup.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MusicApply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MUSIC_APPLY_ID")
    private Long musicApplyId;

    private String title;

    private String artist;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WRITER_ID")
    private User writer;

    @ColumnDefault("0")
    private Integer likeCnt;

    @Builder
    public MusicApply(Long musicApplyId, String title, String artist, String content, User writer, Integer likeCnt) {
        this.musicApplyId = musicApplyId;
        this.title = title;
        this.artist = artist;
        this.content = content;
        this.writer = writer;
        this.likeCnt = likeCnt;
    }
}
