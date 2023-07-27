package com.pi.stepup.domain.rank.domain;

import com.pi.stepup.domain.rank.constant.RankName;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RANK_ID")
    private Long rankId;

    @Enumerated(EnumType.STRING)
    private RankName name;

    private Integer startPoint;

    private Integer endPoint;

    private String rankImg;

    @Builder
    public Rank(RankName name, Integer startPoint, Integer endPoint, String rankImg) {
        this.name = name;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.rankImg = rankImg;
    }
}
