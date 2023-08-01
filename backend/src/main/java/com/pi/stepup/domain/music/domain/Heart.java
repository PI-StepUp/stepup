package com.pi.stepup.domain.music.domain;

import com.pi.stepup.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Heart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HEART_ID")
    private Long heartId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MUSIC_APPLY_ID")
    private MusicApply musicApply;

    @Builder
    public Heart(User user, MusicApply musicApply) {
        this.user = user;
        this.musicApply = musicApply;
    }

    public void setMusicApply(MusicApply musicApply) {
        this.musicApply = musicApply;
    }
}
