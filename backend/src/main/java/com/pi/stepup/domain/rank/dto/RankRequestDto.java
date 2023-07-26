package com.pi.stepup.domain.rank.dto;

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
        private String id;
        private Long pointPolicyId;
        private Long randomDanceId;
        private Integer count;
    }
}
