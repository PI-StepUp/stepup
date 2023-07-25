package com.pi.stepup.domain.dance.dto;

import com.pi.stepup.domain.dance.domain.RandomDance;
import lombok.Builder;
import lombok.Getter;

public class DanceResponseDto {

    @Getter
    public static class DanceSaveResponseDto {

        private final Long randomDanceId;
        //        private final String title;
//        private final String content;
//        private final LocalDateTime startAt;
//        private final LocalDateTime endAt;
//        private final DanceType danceType;
//        private final int maxUser;
//        private final String thumbnail;
        private final String hostName;
//        private final List<DanceMusicFindResponseDto> danceMusicFindResponseDtoList = new ArrayList<>();

        //        private final List<Reservation> reservation = new ArrayList<>();
        @Builder
        private DanceSaveResponseDto(RandomDance randomDance) {
            this.randomDanceId = randomDance.getRandomDanceId();
            this.hostName = randomDance.getHost().getNickname();

//            randomDance.getDanceMusicList()
//                .forEach(oi -> this.danceMusicFindResponseDtoList.add(
//                    new DanceMusicFindResponseDto(oi)));
        }
    }

//    @Getter
//    public static class UserFindResponseDto {
//
//        private final Long userId;
//        private final String id;
//        private final Country country;
//        private final String nickname;
//
//        public UserFindResponseDto(User user) {
//            this.userId = user.getUserId();
//            this.id = user.getId();
//            this.country = user.getCountry();
//            this.nickname = user.getNickname();
//        }
//    }
//
//    @Getter
//    public static class DanceMusicFindResponseDto {
//
//        private final Long musicId;
//        private final String title;
//        private final String artist;
////        private final String answer;
////        private final String URL;
//
//        public DanceMusicFindResponseDto(DanceMusic danceMusic) {
//            this.musicId = danceMusic.getMusic().getMusicId();
//            this.title = danceMusic.getMusic().getTitle();
//            this.artist = danceMusic.getMusic().getArtist();
//        }
//    }

}
