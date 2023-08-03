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
        private final String content;
        private final String writerName;
        private final String writerProfileImg;
        private final Integer heartCnt;

        // TODO : 하트 누를 수 있는지 확인 하는 status
        private Integer canHeart; // 0 이면 이미 좋아요, 1이면 좋아요 누를 수 있음

        @Builder
        public MusicApplyFindResponseDto(MusicApply musicApply, Integer canHeart) {
            this.musicApplyId = musicApply.getMusicApplyId();
            this.title = musicApply.getTitle();
            this.artist = musicApply.getArtist();
            this.content = musicApply.getContent();
            this.writerName = musicApply.getWriter().getNickname();
            this.writerProfileImg = musicApply.getWriter().getProfileImg();
            this.heartCnt = musicApply.getHeartCnt();
            this.canHeart = canHeart;
        }
    }

    @Getter
    public static class MusicApplyJPAFindResponseDto {

        private final Long musicApplyId;
        private final String title;
        private final String artist;
        private final String writerName;
        private final String writerProfileImg;
        private final Integer heartCnt;
        private final Long heartId;

        @Builder
        public MusicApplyJPAFindResponseDto(Long musicApplyId, String title, String artist,
            String writerName, String writerProfileImg, Integer heartCnt, Long heartId) {
            this.musicApplyId = musicApplyId;
            this.title = title;
            this.artist = artist;
            this.writerName = writerName;
            this.writerProfileImg = writerProfileImg;
            this.heartCnt = heartCnt;
            this.heartId = heartId;
        }
    }

//    @Getter
//    public static class AllMusicApplyFindResponseDto {
//
//        private final Long musicApplyId;
//        private final String title;
//        private final String artist;
//        private final String writerName;
//        private final String writerProfileImg;
//        private final Integer heartCnt;
//
//        @Builder
//        public AllMusicApplyFindResponseDto(MusicApply musicApply) {
//            this.musicApplyId = musicApply.getMusicApplyId();
//            this.title = musicApply.getTitle();
//            this.artist = musicApply.getArtist();
//            this.writerName = musicApply.getWriter().getNickname();
//            this.writerProfileImg = musicApply.getWriter().getProfileImg();
//            this.heartCnt = musicApply.getHeartCnt();
//        }
//    }
}
