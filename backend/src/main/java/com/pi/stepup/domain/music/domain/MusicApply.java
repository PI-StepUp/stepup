package com.pi.stepup.domain.music.domain;

import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.global.entity.BaseEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class MusicApply extends BaseEntity {

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
    private Integer heartCnt;

    @OneToMany(mappedBy = "musicApply")
    private List<Heart> hearts = new ArrayList<>();

    @Builder
    public MusicApply(Long musicApplyId, String title, String artist, String content, User writer,
        Integer heartCnt,
        List<Heart> hearts) {
        this.musicApplyId = musicApplyId;
        this.title = title;
        this.artist = artist;
        this.content = content;
        this.writer = writer;
        this.heartCnt = heartCnt;
        this.hearts = hearts;
    }

    public void addHeart() {
        this.heartCnt += 1;
    }

    public void removeHeart() {
        this.heartCnt -= 1;
    }

    public void addHeartAndSetMusicApply(Heart heart) {
        this.hearts.add(heart);
        heart.setMusicApply(this);
    }
}
