package com.pi.stepup.domain.music.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class MusicRequestDto {

    @Getter
    @RequiredArgsConstructor
    public static class MusicSaveRequestDto {
        private final String title;
        private final String artist;
        private final String answer;
        private final String URL;
    }
}
