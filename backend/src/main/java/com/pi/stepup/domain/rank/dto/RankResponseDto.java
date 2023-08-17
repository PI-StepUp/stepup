package com.pi.stepup.domain.rank.dto;

import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.rank.constant.PointType;
import com.pi.stepup.domain.rank.constant.RankName;
import com.pi.stepup.domain.rank.domain.PointHistory;
import com.pi.stepup.domain.rank.domain.PointPolicy;
import com.pi.stepup.domain.rank.domain.Rank;
import lombok.Builder;
import lombok.Getter;

public class RankResponseDto {

    @Getter
    public static class PointHistoryFindResponseDto {

        private final PointType pointType;
        private final Integer point;
        private final Integer count;
        private Long randomDanceId;
        private String randomDanceTitle;

        @Builder
        public PointHistoryFindResponseDto(PointHistory pointHistory) {
            PointPolicy pointPolicy = pointHistory.getPointPolicy();
            RandomDance randomDance = pointHistory.getRandomDance();

            this.pointType = pointPolicy.getPointType();
            this.point = pointPolicy.getPoint() * pointHistory.getCount();
            if (randomDance != null) {
                this.randomDanceId = randomDance.getRandomDanceId();
                this.randomDanceTitle = randomDance.getTitle();
            }
            this.count = pointHistory.getCount();
        }
    }

    @Getter
    public static class UserRankFindResponseDto {

        private final RankName rankName;
        private final String rankImg;

        @Builder
        public UserRankFindResponseDto(Rank rank) {
            this.rankImg = rank.getRankImg();
            this.rankName = rank.getName();
        }
    }
}
