package com.pi.stepup.domain.dance.dto;

import com.pi.stepup.domain.dance.constant.DanceType;
import com.pi.stepup.domain.dance.domain.RandomDance;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

public class DanceRequestDto {

    @Getter
    public static class DanceSaveRequestDto {

        private Long randomDanceId;
        private String title;
        private String content;
        private LocalDateTime startAt;
        private LocalDateTime endAt;
        private DanceType danceType;
        private int maxUser;
        private String thumbnail;

        @Builder
        private DanceSaveRequestDto(RandomDance randomDance) {
            this.randomDanceId = randomDance.getId();
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
