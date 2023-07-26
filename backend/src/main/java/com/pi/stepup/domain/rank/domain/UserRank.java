package com.pi.stepup.domain.rank.domain;

import com.pi.stepup.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_RANK")
    private Long userRankId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Rank rank;

    @Builder
    public UserRank(User user, Rank rank) {
        this.user = user;
        this.rank = rank;
    }

    // userRank가 주인
    public void setUserAndSetThis(User user) {
        this.user = user;
        user.setRank(this);
    }
}
