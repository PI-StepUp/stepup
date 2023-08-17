package com.pi.stepup.domain.music.domain;

import com.pi.stepup.global.entity.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Music extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MUSIC_ID")
    private Long musicId;

    private String title;

    private String artist;

    private String answer;

    private String URL;

    private Integer playtime;

    @Builder
    public Music(Long musicId, String title, String artist, String answer, String URL,
        Integer playtime) {
        this.musicId = musicId;
        this.title = title;
        this.artist = artist;
        this.answer = answer;
        this.URL = URL;
        this.playtime = playtime;
    }

    public void updateMusicInfo(Music music) {
        if (StringUtils.hasText(music.getTitle())) {
            this.title = music.getTitle();
        }

        if (StringUtils.hasText(music.getArtist())) {
            this.artist = music.getArtist();
        }

        if (StringUtils.hasText(music.getURL())) {
            this.URL = music.getURL();
        }

        if (music.getPlaytime() != null && music.getPlaytime() > 0) {
            this.playtime = music.getPlaytime();
        }
    }

    public void setAnswerAsMusicAnswerId(String musicAnswerId) {
        this.answer = musicAnswerId;
    }
}
