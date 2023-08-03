package com.pi.stepup.global.sampledata;

import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.rank.constant.PointType;
import com.pi.stepup.domain.rank.constant.RankName;
import com.pi.stepup.domain.rank.domain.PointPolicy;
import com.pi.stepup.domain.rank.domain.Rank;
import com.pi.stepup.domain.user.domain.Country;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;
        private final PasswordEncoder passwordEncoder;

        public void dbInit() {
            makeCountry();
            makeMusic();
            makePoint();
            makeRank();
            makeAdmin();
            makeUser();
            makeDance();
            makeDanceMusic();
            makeReservation();
            makeAttend();
        }

        public void makeCountry() {
            Country country1 = Country.builder().code("KO").build();
            Country country2 = Country.builder().code("US").build();
            Country country3 = Country.builder().code("CN").build();
            Country country4 = Country.builder().code("JP").build();
            Country country5 = Country.builder().code("DE").build();
            Country country6 = Country.builder().code("GB").build();
            Country country7 = Country.builder().code("FR").build();

            em.persist(country1);
            em.persist(country2);
            em.persist(country3);
            em.persist(country4);
            em.persist(country5);
            em.persist(country6);
            em.persist(country7);
        }

        public void makeMusic() {
            Music music1 = Music.builder().title("ISTJ").artist("NCT DREAM")
                .answer("answer").URL("url").build();
            Music music2 = Music.builder().title("Broken Melodies").artist("NCT DREAM")
                .answer("answer").URL("url").build();
            Music music3 = Music.builder().title("Yogurt Shake").artist("NCT DREAM")
                .answer("answer").URL("url").build();
            Music music4 = Music.builder().title("Skateboard").artist("NCT DREAM")
                .answer("answer").URL("url").build();
            Music music5 = Music.builder().title("Blue Wave").artist("NCT DREAM")
                .answer("answer").URL("url").build();
            Music music6 = Music.builder().title("Poison").artist("NCT DREAM")
                .answer("answer").URL("url").build();
            Music music7 = Music.builder().title("SOS").artist("NCT DREAM")
                .answer("answer").URL("url").build();
            Music music8 = Music.builder().title("Pretzel").artist("NCT DREAM")
                .answer("answer").URL("url").build();
            Music music9 = Music.builder().title("Starry Night").artist("NCT DREAM")
                .answer("answer").URL("url").build();
            Music music10 = Music.builder().title("Like We Just Met").artist("NCT DREAM")
                .answer("answer").URL("url").build();

            em.persist(music1);
            em.persist(music2);
            em.persist(music3);
            em.persist(music4);
            em.persist(music5);
            em.persist(music6);
            em.persist(music7);
            em.persist(music8);
            em.persist(music9);
            em.persist(music10);
        }

        public void makePoint() {
            PointPolicy pointPolicy1 = PointPolicy.builder()
                .pointType(PointType.valueOf("FIRST_PRIZE")).point(100).build();
            PointPolicy pointPolicy2 = PointPolicy.builder()
                .pointType(PointType.valueOf("SECOND_PRIZE")).point(50).build();
            PointPolicy pointPolicy3 = PointPolicy.builder()
                .pointType(PointType.valueOf("THIRD_PRIZE")).point(30).build();
            PointPolicy pointPolicy4 = PointPolicy.builder()
                .pointType(PointType.valueOf("SUCCESS_MUSIC")).point(5).build();
            PointPolicy pointPolicy5 = PointPolicy.builder()
                .pointType(PointType.valueOf("OPEN_DANCE")).point(50).build();
            PointPolicy pointPolicy6 = PointPolicy.builder()
                .pointType(PointType.valueOf("PRACTICE_ROOM")).point(3).build();

            em.persist(pointPolicy1);
            em.persist(pointPolicy2);
            em.persist(pointPolicy3);
            em.persist(pointPolicy4);
            em.persist(pointPolicy5);
            em.persist(pointPolicy6);
        }

        public void makeRank() {
            Rank rank1 = Rank.builder().name(RankName.valueOf("BRONZE"))
                .startPoint(0).endPoint(99).rankImg("url").build();
            Rank rank2 = Rank.builder().name(RankName.valueOf("SILVER"))
                .startPoint(100).endPoint(299).rankImg("url").build();
            Rank rank3 = Rank.builder().name(RankName.valueOf("GOLD"))
                .startPoint(300).endPoint(999).rankImg("url").build();
            Rank rank4 = Rank.builder().name(RankName.valueOf("PLATINUM"))
                .startPoint(1000).endPoint(5000).rankImg("url").build();

            em.persist(rank1);
            em.persist(rank2);
            em.persist(rank3);
            em.persist(rank4);
        }

        public void makeAdmin() {
            em.createNativeQuery
                    ("insert into users (user_id, created_at, modified_at, birth, country_id,"
                        + " email, email_alert, id, nickname, password, point, profile_img, rank_id,"
                        + " refresh_token, role) "
                        + " values (1, '2023-08-03', '2023-08-03', '1997-01-01', 1, 'ADMIN@naver.com',"
                        + " 1, 'admin', 'admin', 'password', 0, '', 4, '', 'ROLE_ADMIN')")
                .executeUpdate();
        }

        public void makeUser() {
            em.createNativeQuery
                    ("insert into users (user_id, created_at, modified_at, birth, country_id,"
                        + " email, email_alert, id, nickname, password, point, profile_img, rank_id,"
                        + " refresh_token, role) "
                        + " values (2, '2023-08-03', '2023-08-03', '1997-01-01', 1, 'testId1@naver.com',"
                        + " 1, 'testId1', 'testNick1', 'testPassword1', 0, '', 1, '', 'ROLE_USER')")
                .executeUpdate();
            em.createNativeQuery
                    ("insert into users (user_id, created_at, modified_at, birth, country_id,"
                        + " email, email_alert, id, nickname, password, point, profile_img, rank_id,"
                        + " refresh_token, role) "
                        + " values (3, '2023-08-03', '2023-08-03', '1997-01-01', 1, 'testId2@naver.com',"
                        + " 1, 'testId2', 'testNick2', 'testPassword2', 0, '', 1, '', 'ROLE_USER')")
                .executeUpdate();
        }

        public void makeDance() {
            em.createNativeQuery
                    ("insert into random_dance (random_dance_id, content, dance_type,"
                        + " end_at, user_id, max_user, start_at, thumbnail, title) "
                        + " values (1, '함께 합시다', 'RANKING', '2023-08-31 18:00', 2, 10,"
                        + " '2023-08-01 10:00', '', '제1회 랜덤 플레이 댄스')")
                .executeUpdate();
            em.createNativeQuery
                    ("insert into random_dance (random_dance_id, content, dance_type,"
                        + " end_at, user_id, max_user, start_at, thumbnail, title) "
                        + " values (2, '함께 합시다', 'BASIC', '2023-08-31 18:00', 3, 10,"
                        + " '2023-08-20 10:00', '', '제2회 랜덤 플레이 댄스')")
                .executeUpdate();
        }

        public void makeDanceMusic() {
            em.createNativeQuery
                    ("insert into dance_music (DANCE_MUSIC_ID, MUSIC_ID, RANDOM_DANCE_ID) "
                        + " values (1, 1, 1)")
                .executeUpdate();
            em.createNativeQuery
                    ("insert into dance_music (DANCE_MUSIC_ID, MUSIC_ID, RANDOM_DANCE_ID) "
                        + " values (2, 2, 1)")
                .executeUpdate();
            em.createNativeQuery
                    ("insert into dance_music (DANCE_MUSIC_ID, MUSIC_ID, RANDOM_DANCE_ID) "
                        + " values (3, 3, 1)")
                .executeUpdate();
            em.createNativeQuery
                    ("insert into dance_music (DANCE_MUSIC_ID, MUSIC_ID, RANDOM_DANCE_ID) "
                        + " values (4, 4, 2)")
                .executeUpdate();
            em.createNativeQuery
                    ("insert into dance_music (DANCE_MUSIC_ID, MUSIC_ID, RANDOM_DANCE_ID) "
                        + " values (5, 5, 2)")
                .executeUpdate();
            em.createNativeQuery
                    ("insert into dance_music (DANCE_MUSIC_ID, MUSIC_ID, RANDOM_DANCE_ID) "
                        + " values (6, 6, 2)")
                .executeUpdate();
        }

        public void makeReservation() {
            em.createNativeQuery
                    ("insert into reservation (RESERVATION_ID, RANDOM_DANCE_ID, USER_ID) "
                        + " values (1, 2, 2)")
                .executeUpdate();
            em.createNativeQuery
                    ("insert into reservation (RESERVATION_ID, RANDOM_DANCE_ID, USER_ID) "
                        + " values (2, 1, 3)")
                .executeUpdate();
        }

        public void makeAttend() {
            em.createNativeQuery
                    ("insert into attend_history (ATTEND_HISTORY_ID, RANDOM_DANCE_ID, USER_ID) "
                        + " values (1, 1, 2)")
                .executeUpdate();
            em.createNativeQuery
                    ("insert into attend_history (ATTEND_HISTORY_ID, RANDOM_DANCE_ID, USER_ID) "
                        + " values (2, 2, 2)")
                .executeUpdate();
            em.createNativeQuery
                    ("insert into attend_history (ATTEND_HISTORY_ID, RANDOM_DANCE_ID, USER_ID) "
                        + " values (3, 1, 3)")
                .executeUpdate();
            em.createNativeQuery
                    ("insert into attend_history (ATTEND_HISTORY_ID, RANDOM_DANCE_ID, USER_ID) "
                        + " values (4, 2, 3)")
                .executeUpdate();
        }

//        public void makeUser() {
//            for (int i = 0; i < 5; i++) {
//                SignUpRequestDto signUpRequestDto2
//                    = SignUpRequestDto.builder()
//                    .id(id + i)
//                    .password(password)
//                    .email(id + i + email)
//                    .countryId(1L)
//                    .emailAlert(1)
//                    .nickname(nick + i)
//                    .birth(birth)
//                    .profileImg("")
//                    .role(UserRole.ROLE_USER)
//                    .build();
//
//                user = signUpRequestDto2.toUser(
//                    passwordEncoder.encode(signUpRequestDto2.getPassword()),
//                    country);
//
//                user.setRank(rank);
//                user.setPointZero();
//
//                em.persist(user);
//            }
//        }
//
//        public void makeAdmin() {
//            SignUpRequestDto signUpRequestDto
//                = SignUpRequestDto.builder()
//                .id(admin)
//                .password(admin)
//                .email(admin + email)
//                .countryId(1L)
//                .emailAlert(1)
//                .nickname(admin)
//                .birth(birth)
//                .profileImg("")
//                .role(UserRole.ROLE_ADMIN)
//                .build();
//
//            User administrator = signUpRequestDto.toUser(
//                passwordEncoder.encode(signUpRequestDto.getPassword()),
//                country);
//
//            administrator.setRank(rank);
//            administrator.setPointZero();
//            em.persist(administrator);
//        }

    }
}