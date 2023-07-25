package com.pi.stepup.domain.dance.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pi.stepup.domain.dance.constant.DanceType;
import com.pi.stepup.domain.dance.domain.RandomDance;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

public class DanceRequestDto {

    @Getter
    @NoArgsConstructor
    public static class DanceSaveRequestDto {

        private Long randomDanceId;
        private String title;
        private String content;
        @DateTimeFormat
        private LocalDateTime startAt;
        @DateTimeFormat
        private LocalDateTime endAt;
        private DanceType danceType;
        private int maxUser;
        private String thumbnail;

        @Builder
        private DanceSaveRequestDto(RandomDance randomDance) {
            this.randomDanceId = randomDance.getRandomDanceId();
            this.title = randomDance.getTitle();
            this.content = randomDance.getContent();
            this.startAt = randomDance.getStartAt();
            this.endAt = randomDance.getEndAt();
            this.danceType = randomDance.getDanceType();
            this.maxUser = randomDance.getMaxUser();
            this.thumbnail = randomDance.getThumbnail();
        }
    }

}
