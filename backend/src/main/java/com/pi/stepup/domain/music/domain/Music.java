package com.pi.stepup.domain.music.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Music {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MUSIC_ID")
    private Long musicId;

    private String title;

    private String artist;

    private String answer; // 배열로 바꿀 것

    private String URL;

    @Builder
    public Music(Long musicId, String title, String artist, String answer, String URL) {
        this.musicId = musicId;
        this.title = title;
        this.artist = artist;
        this.answer = answer;
        this.URL = URL;
    }
}
