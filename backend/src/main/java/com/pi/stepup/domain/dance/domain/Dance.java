package com.pi.stepup.domain.dance.domain;

import com.pi.stepup.domain.dance.constant.DanceType;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "RandomPlayDance")
@Getter
@NoArgsConstructor
public class Dance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "randomPlayDanceId")
    private Long id;

    private String title;

    private String content;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    @Enumerated(EnumType.STRING)
    private DanceType danceType;

    private int maxUser;

    private String thumbnail;

    //private User host;

    @OneToMany(mappedBy = "playlistId")
    private List<Playlist> playlist;

    @Builder
    public Dance(String title, String content, LocalDateTime startAt, LocalDateTime endAt,
        DanceType danceType, int maxUser, String thumbnail) {
        this.title = title;
        this.content = content;
        this.startAt = startAt;
        this.endAt = endAt;
        this.danceType = danceType;
        this.maxUser = maxUser;
        this.thumbnail = thumbnail;
    }
}
