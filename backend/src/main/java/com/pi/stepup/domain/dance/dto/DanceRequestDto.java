package com.pi.stepup.domain.dance.dto;

import com.pi.stepup.domain.dance.constant.DanceType;
import com.pi.stepup.domain.dance.domain.RandomDance;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class DanceRequestDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DanceSaveRequestDto {

        private Long randomDanceId;
        private String title;
        private String content;
        private LocalDateTime startAt;
        private LocalDateTime endAt;
        private DanceType danceType;
        private int maxUser;
        private String thumbnail;
    }

}
