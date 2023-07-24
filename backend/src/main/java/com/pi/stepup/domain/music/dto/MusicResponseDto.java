package com.pi.stepup.domain.music.dto;

import com.pi.stepup.domain.music.domain.Music;
import lombok.Builder;
import lombok.Getter;

public class MusicResponseDto {

    @Getter
    public static class MusicFindResponseDto {
        private final Long musicId;
        private final String title;
        private final String artist;
        private final String answer;
        private final String URL;

        @Builder
        public MusicFindResponseDto(Music music) {
            this.musicId = music.getMusicId();
            this.title = music.getTitle();
            this.artist = music.getArtist();
            this.answer = music.getAnswer();
            this.URL = music.getURL();
        }
    }
}
