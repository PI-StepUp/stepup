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
public class MusicRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MUSIC_REQUEST_ID")
    private Long musicRequestId;

    private String title;

    private String artist;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USERD_ID")
    private User user;

    @ColumnDefault("0")
    private Integer likeCnt;

    @Builder
    public MusicRequest(Long musicRequestId, String title, String artist, String content, User user, Integer likeCnt) {
        this.musicRequestId = musicRequestId;
        this.title = title;
        this.artist = artist;
        this.content = content;
        this.user = user;
        this.likeCnt = likeCnt;
    }
}
