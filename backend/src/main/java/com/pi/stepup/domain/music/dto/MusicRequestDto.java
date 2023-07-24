package com.pi.stepup.domain.music.dto;

import com.pi.stepup.domain.music.domain.Music;
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
        private Long musicId;
        private String title;
        private String artist;
        private String answer;
        private String URL;

        public Music toEntity() {
            return Music.builder()
                    .musicId(this.musicId)
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
        private String writerId;
        private String title;
        private String artist;
        private String content;
    }

}
