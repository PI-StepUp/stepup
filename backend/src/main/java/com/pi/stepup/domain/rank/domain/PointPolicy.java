package com.pi.stepup.domain.rank.domain;

import com.pi.stepup.domain.rank.constant.PointType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POINT_POLICY_ID")
    private Long pointPolicyId;

    @Enumerated(EnumType.STRING)
    private PointType pointType;

    private Integer point;

    @Builder
    public PointPolicy(PointType pointType, Integer point) {
        this.pointType = pointType;
        this.point = point;
    }
}
