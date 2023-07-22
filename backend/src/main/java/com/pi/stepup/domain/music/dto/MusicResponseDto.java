package com.pi.stepup.domain.music.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MusicResponseDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MusicFindResponseDto {
        private String title;
        private String artist;
        private String answer;
        private String URL;
    }
}
