package com.pi.stepup.domain.dance.domain;

import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.global.entity.BaseEntity;
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
@Table(name = "ATTEND_HISTORY")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttendHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendHistoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RANDOM_DANCE_ID")
    private RandomDance randomDance;

    @Builder
    public AttendHistory(Long attendHistoryId, User user, RandomDance randomDance) {
        this.attendHistoryId = attendHistoryId;
        this.user = user;
        this.randomDance = randomDance;
    }
}
