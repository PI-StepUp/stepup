package com.pi.stepup.domain.dance.dto;

import com.pi.stepup.domain.dance.constant.DanceType;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.user.domain.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        private String startAt;
        private String endAt;
        private DanceType danceType;
        private int maxUser;
        private String thumbnail;
        private User host;

        public RandomDance toEntity() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            return RandomDance.builder()
                .randomDanceId(this.randomDanceId)
                .title(this.title)
                .content(this.content)
                .startAt(LocalDateTime.parse(this.startAt, formatter))
                .endAt(LocalDateTime.parse(this.endAt, formatter))
                .danceType(this.danceType)
                .maxUser(this.maxUser)
                .thumbnail(this.thumbnail)
                .host(this.host)
                .build();
        }
    }

}