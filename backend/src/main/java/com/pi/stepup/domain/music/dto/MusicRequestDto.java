package com.pi.stepup.domain.music.dto;

import com.pi.stepup.domain.music.domain.Music;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
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
