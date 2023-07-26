package com.pi.stepup.domain.dance.dto;

import com.pi.stepup.domain.dance.constant.DanceType;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.user.domain.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
        private String title;
        private String content;
        private String startAt;
        private String endAt;
        private DanceType danceType;
        private int maxUser;
        private String thumbnail;
        private String hostId;
        private List<Long> danceMusicIdList = new ArrayList<>();

        public RandomDance toEntity(User host) {
            return RandomDance.builder()
                .title(this.title)
                .content(this.content)
                .startAt(LocalDateTime.parse(this.startAt, formatter))
                .endAt(LocalDateTime.parse(this.endAt, formatter))
                .danceType(this.danceType)
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
        private Long randomDanceId;
        private String title;
        private String content;
        private String startAt;
        private String endAt;
        private DanceType danceType;
        private int maxUser;
        private String thumbnail;
        private String hostId;
        private List<Long> danceMusicIdList = new ArrayList<>();

        public RandomDance toEntity(User host) {
            return RandomDance.builder()
                .title(this.title)
                .content(this.content)
                .startAt(LocalDateTime.parse(this.startAt, formatter))
                .endAt(LocalDateTime.parse(this.endAt, formatter))
                .danceType(this.danceType)
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
    public static class DanceReserveRequestDto {
        private String id;
        private Long randomDanceId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DanceAttendRequestDto {
        private String id;
        private Long randomDanceId;
    }
}