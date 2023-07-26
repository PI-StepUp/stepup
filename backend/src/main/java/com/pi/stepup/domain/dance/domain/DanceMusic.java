package com.pi.stepup.domain.dance.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pi.stepup.domain.music.domain.Music;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "DANCE_MUSIC")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DanceMusic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DANCE_MUSIC_ID")
    private Long danceMusicId;

    //    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MUSIC_ID")
    private Music music;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RANDOM_DANCE_ID")
    private RandomDance randomDance;

    public void setRandomDance(RandomDance randomDance) {
        this.randomDance = randomDance;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public static DanceMusic createDanceMusic(Music music) {
        DanceMusic danceMusic = new DanceMusic();
        danceMusic.setMusic(music);
        return danceMusic;
    }

    @Override
    public String toString() {
        return "DanceMusic{" +
            "music=" + music +
            '}';
    }
}
