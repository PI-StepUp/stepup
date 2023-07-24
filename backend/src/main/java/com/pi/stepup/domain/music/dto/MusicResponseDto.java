package com.pi.stepup.domain.music.dto;

import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.music.domain.MusicApply;
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

    @Getter
    public static class MusicApplyFindResponseDto {
        private final Long musicApplyId;
        private final String title;
        private final String artist;
        private final String writerName;
        private final String writerProfileImg;
        private final Integer likeCnt;

        @Builder
        public MusicApplyFindResponseDto(MusicApply musicApply){
            this.musicApplyId = musicApply.getMusicRequestId();
            this.title = musicApply.getTitle();
            this.artist = musicApply.getArtist();
            this.writerName = musicApply.getWriter().getNickname();
            this.writerProfileImg = musicApply.getWriter().getProfileImg();
            this.likeCnt = musicApply.getLikeCnt();
        }
    }
}
