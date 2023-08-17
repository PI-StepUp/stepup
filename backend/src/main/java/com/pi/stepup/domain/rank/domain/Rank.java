package com.pi.stepup.domain.rank.domain;

import com.pi.stepup.domain.rank.constant.RankName;
import com.pi.stepup.global.entity.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "RANKS")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rank extends BaseEntity {

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
