package com.pi.stepup.domain.dance.domain;

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
import lombok.Builder;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RANDOM_DANCE_ID")
    private RandomDance randomDance;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "MUSIC_ID")
//    private Music music;

    @Builder
    public DanceMusic(Long id, RandomDance randomDance) {
        this.danceMusicId = id;
        this.randomDance = randomDance;
    }
}
