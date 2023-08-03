package com.pi.stepup.domain.music.dto;

import com.pi.stepup.domain.music.domain.Heart;
import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.music.domain.MusicApply;
import com.pi.stepup.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MusicRequestDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MusicSaveRequestDto {

        private String title;
        private String artist;
        private String answer;
        private String URL;

        public Music toEntity() {
            return Music.builder()
                .title(this.title)
                .artist(this.artist)
                .answer(this.answer)
                .URL(this.URL)
                .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MusicApplySaveRequestDto {

        private String title;
        private String artist;
        private String content;

        public MusicApply toEntity(User user) {
            return MusicApply.builder()
                .writer(user)
                .artist(this.artist)
                .title(this.title)
                .content(this.content)
                .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HeartSaveRequestDto {

        private Long musicApplyId;

        public Heart toEntity(User user, MusicApply musicApply) {
            return Heart.builder()
                .user(user)
                .musicApply(musicApply)
                .build();
        }
    }
}
