package com.pi.stepup.domain.rank.domain;

import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POINT_HISTORY_ID")
    private Long pointHistoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private PointPolicy pointPolicy;

    @ManyToOne(fetch = FetchType.LAZY)
    private RandomDance randomDance;

    private Integer count;

    @Builder
    public PointHistory(User user, PointPolicy pointPolicy, RandomDance randomDance, Integer count) {
        this.user = user;
        this.pointPolicy = pointPolicy;
        this.randomDance = randomDance;
        this.count = count;
    }
}
