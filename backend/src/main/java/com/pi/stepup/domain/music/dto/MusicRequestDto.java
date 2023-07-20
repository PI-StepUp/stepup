package com.pi.stepup.domain.music.dto;

import com.pi.stepup.domain.music.domain.Music;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MusicRequestDto {

    @Getter
    @NoArgsConstructor
    public static class MusicSaveRequestDto {
        private String title;
        private String artist;
        private String answer;
        private String URL;

        @Builder
        public MusicSaveRequestDto(String title, String artist, String answer, String URL) {
            this.title = title;
            this.artist = artist;
            this.answer = answer;
            this.URL = URL;
        }

        public Music toEntity(){
            return Music.builder()
                    .title(this.title)
                    .artist(this.artist)
                    .answer(this.answer)
                    .URL(this.URL)
                    .build();
        }
    }


}
