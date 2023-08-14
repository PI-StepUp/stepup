package com.pi.stepup.domain.dance.dto;

import com.pi.stepup.domain.dance.constant.DanceType;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.user.domain.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public class DanceRequestDto {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
        "yyyy-MM-dd HH:mm");

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DanceCreateRequestDto {

        @NotBlank
        private String title;
        @NotBlank
        private String content;
        @NotBlank
        private String startAt;
        @NotBlank
        private String endAt;
        @NotBlank
        private String danceType;
        @NotNull
        private int maxUser;
        private String thumbnail;
        @NotBlank
        private String hostId;
        @NotNull
        private List<Long> danceMusicIdList;

        public RandomDance toEntity(User host) {
            return RandomDance.builder()
                .title(this.title)
                .content(this.content)
                .startAt(LocalDateTime.parse(this.startAt, formatter))
                .endAt(LocalDateTime.parse(this.endAt, formatter))
                .danceType(DanceType.valueOf(danceType))
                .maxUser(this.maxUser)
                .thumbnail(this.thumbnail)
                .host(host)
                .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DanceUpdateRequestDto {

        @NotNull
        private Long randomDanceId;
        @NotBlank
        private String title;
        @NotBlank
        private String content;
        @NotBlank
        private String startAt;
        @NotBlank
        private String endAt;
        @NotBlank
        private String danceType;
        @NotNull
        private int maxUser;
        private String thumbnail;
        @NotBlank
        private String hostId;
        @NotNull
        private List<Long> danceMusicIdList;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DanceSearchRequestDto {

        private String progressType;
        private String keyword;
    }
}