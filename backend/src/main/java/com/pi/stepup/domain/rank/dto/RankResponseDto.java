package com.pi.stepup.domain.rank.dto;

import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.rank.constant.PointType;
import com.pi.stepup.domain.rank.domain.PointHistory;
import com.pi.stepup.domain.rank.domain.PointPolicy;
import lombok.Builder;
import lombok.Getter;

public class RankResponseDto {
    @Getter
    public static class PointHistoryFindResponseDto {
        private final PointType pointType;
        private final Integer point;
        private final Long randomDanceId;
        private final String randomDanceTitle;
        private final Integer count;

        @Builder
        public PointHistoryFindResponseDto(PointHistory pointHistory) {
            PointPolicy pointPolicy = pointHistory.getPointPolicy();
            RandomDance randomDance = pointHistory.getRandomDance();

            this.pointType = pointPolicy.getPointType();
            // TODO : 포인트 합산 했는데 count도 따로 전달해줘서 이 부분 상의해봐야 할 듯
            this.point = pointPolicy.getPoint() * pointHistory.getCount();
            this.randomDanceId = randomDance.getRandomDanceId();
            this.randomDanceTitle = randomDance.getTitle();
            this.count = pointHistory.getCount();
        }
    }
}
