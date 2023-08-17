package com.pi.stepup.domain.rank.dto;

import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.rank.domain.PointHistory;
import com.pi.stepup.domain.rank.domain.PointPolicy;
import com.pi.stepup.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class RankRequestDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PointUpdateRequestDto {

        private Long pointPolicyId;
        private Long randomDanceId;
        private Integer count;

        public PointHistory toEntity(User user, PointPolicy pointPolicy, RandomDance randomDance) {
            return PointHistory.builder()
                .pointPolicy(pointPolicy)
                .randomDance(randomDance)
                .count(this.count)
                .user(user)
                .build();
        }
    }
}
