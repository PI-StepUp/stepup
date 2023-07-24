package com.pi.stepup.domain.dance.domain;

import com.pi.stepup.domain.dance.constant.DanceType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "RANDOM_DANCE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RandomDance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RANDOM_DANCE_ID")
    private Long id;

    private String title;

    private String content;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    @Enumerated(EnumType.STRING)
    private DanceType danceType;

    private int maxUser;

    private String thumbnail;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "USER_ID")
//    private User host;

    @OneToMany(mappedBy = "randomDance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DanceMusic> danceMusicList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "RESERVATION_ID")
    private Reservation reservation;

    //1:1 양방향
    public void setReservationAndSetThis(Reservation reservation) {
        this.reservation = reservation;
        reservation.setRandomDanceAndSetThis(this);
    }

    @Builder
    public RandomDance(Long id, String title, String content, LocalDateTime startAt,
        LocalDateTime endAt, DanceType danceType, int maxUser, String thumbnail) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.startAt = startAt;
        this.endAt = endAt;
        this.danceType = danceType;
        this.maxUser = maxUser;
        this.thumbnail = thumbnail;
    }
}
