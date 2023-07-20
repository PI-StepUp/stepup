package com.pi.stepup.domain.music.dto;

import com.pi.stepup.domain.music.domain.Music;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class MusicResponseDto {

    @Getter
    private static class MusicFindResponseDto {
        private final String title;
        private final String artist;
        private final String answer;
        private final String URL;

        public MusicFindResponseDto(Music music) {
            this.title = music.getTitle();
            this.artist = music.getArtist();
            this.answer = music.getAnswer();
            this.URL = music.getURL();
        }
    }
}
