package com.pi.stepup.global.sampledata;

import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.rank.constant.PointType;
import com.pi.stepup.domain.rank.constant.RankName;
import com.pi.stepup.domain.rank.domain.PointPolicy;
import com.pi.stepup.domain.rank.domain.Rank;
import com.pi.stepup.domain.user.domain.Country;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Profile("!danceRepoTest")
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
            makeReservation();
            makeAttend();
            makeMusicApply();
            makeDanceMusic();
            makePointHistory();
            makeHeart();
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
            String password = passwordEncoder.encode("admin");
            String sql = "insert into users (user_id, created_at, modified_at, birth, country_id,"
                + " email, email_alert, id, nickname, password, point, profile_img, rank_id,"
                + " refresh_token, role) "
                + " values (1, '2023-01-01', '2023-01-01', '1997-01-01', 1, 'admin@naver.com',"
                + " 1, 'admin', 'admin', ?, 0, 'url', 4, 'refresh_token', 'ROLE_ADMIN')";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, password);
            query.executeUpdate();
        }

        public void makeUser() {
            String[] id = new String[]{"ssafy", "seul", "kyung", "beom", "zu", "choi", "song"};
            String[] nickname = new String[]{"김싸피", "seul1219", "kk0216", "j3beom", "zuuuu_e",
                "inacrown_0", "song_sangsung"};
            String password = passwordEncoder.encode("ssafy");
            for (int i = 0; i < 7; i++) {
                String sql =
                    "insert into users (user_id, created_at, modified_at, birth, country_id, "
                        + "  email, email_alert, id, nickname, password, point, profile_img, rank_id, refresh_token, role) "
                        + " values (?, '2023-08-01', '2023-08-01', '1997-01-01', 1, '"
                        + id[i]
                        + "@naver.com', 1, ?, ?, ?, 0, 'url', 1, 'refresh_token', 'ROLE_ADMIN')";
                Query query = em.createNativeQuery(sql);
                query.setParameter(1, (i + 2));
                query.setParameter(2, id[i]);
                query.setParameter(3, nickname[i]);
                query.setParameter(4, password);
                query.executeUpdate();
            }
        }

        public void makeDance() {
            String[] title = new String[]
                {"세상을 흔들어라! 오늘은 우리의 랜덤 댄스 파티 날!"
                    , "움직임의 마법! 랜덤으로 펼쳐지는 춤의 세계"
                    , "당신의 몸을 주사위처럼 굴려보세요! 랜덤 댄스의 모든 것"
                    , "힘을 내어 춤추라! 오늘은 우주적 랜덤 댄스 대회!"
                    , "춤 속의 모험, 랜덤한 리듬에 맞춰 떠나는 여행"
                    , "하루의 스트레스를 흔들어놓을 때! 랜덤 댄스 터치"
                    , "비트가 주는 설렘! 랜덤 음악에 신나게 몸을 맡겨요"
                    , "춤에 녹아드는 순간, 랜덤 선택이 가져다주는 즐거움"
                    , "함께라면 더 즐거운! 랜덤 파티로 초대합니다"
                    , "춤의 연결고리, 우리의 하모니! 랜덤 댄스로 만나요"};
            String[] content = new String[]
                {"모든 준비가 끝났어요! 랜덤 댄스 파티가 시작됩니다. 함께 즐기며 세상을 흔들어봐요. 마음을 열고 새로운 친구들과 함께 춤을 추며 특별한 순간을 만들어보세요."
                    ,
                    "춤은 마법과 같은 힘을 갖고 있어요. 랜덤으로 선택된 리듬과 움직임으로 펼쳐지는 춤의 세계에 빠져보세요. 감각적인 음악과 함께 신나게 춤을 즐기며 일상을 잠시 잊어보는 건 어떨까요?"
                    ,
                    "오늘은 주사위처럼 몸을 굴려서 랜덤으로 선택된 춤을 추는 놀라운 경험을 해보세요! 어떤 스타일이 나올지 미리 예상할 수 없는 재미와 설렘으로 가득찬 하루를 보낼 준비가 되셨나요?"
                    ,
                    "우주적인 에너지로 물들어진 랜덤 댄스 대회가 찾아왔어요! 빛나는 별 아래서 펼쳐지는 이 축제에 함께해보세요. 다양한 음악에 맞춰 춤을 추며 최고의 댄서가 되어보는 건 어떨까요?"
                    ,
                    "음악의 흐름에 맞춰 랜덤한 리듬으로 여행을 떠나보세요. 각기 다른 춤 스타일을 탐험하며 새로운 경험과 만남을 만들어보는 것은 어떨까요? 모험심을 자극하는 이 춤의 여정에 동참해보세요!"
                    ,
                    "힘들고 지친 하루를 랜덤 댄스로 흔들어보세요. 음악에 몸을 맡기며 스트레스와 피로를 떨쳐내는 시간을 가져보는 건 어떨까요? 즐거운 음악과 함께 춤추며 새로운 활력을 얻어보세요."
                    ,
                    "비트와 리듬이 느껴지는 음악에 몸을 맡겨보세요. 랜덤하게 선택된 곡들과 함께 춤추며 신나는 시간을 만끽해보세요. 음악의 마법에 빠져들어 설렘을 느껴보는 것은 어떨까요?"
                    ,
                    "랜덤으로 선택된 춤에 몸을 맡기면 예상치 못한 즐거움이 찾아올지도 모릅니다. 춤에 녹아드는 순간을 느끼며 랜덤 선택의 특별한 매력을 경험해보세요. 당신의 몸이 이야기하는 순간을 함께해보는 건 어떨까요?"
                    ,
                    "랜덤 댄스 파티에 여러분을 초대합니다! 함께 모여 즐겁게 춤을 추며 특별한 시간을 만들어보세요. 새로운 친구들과의 만남과 함께하는 즐거움을 놓치지 마세요."
                    ,
                    "음악과 춤이 만들어내는 하모니를 느껴보세요. 서로 다른 춤 스타일을 통해 연결되는 랜덤 댄스의 매력을 느껴보며, 우리의 다양성과 개성이 어떻게 아름다운 하모니를 이루는지 함께 탐색해보세요."
                };

            //랭킹
            for (int i = 0; i < 5; i++) {
                String sql =
                    "insert into random_dance (random_dance_id, content, dance_type,"
                        + " end_at, user_id, max_user, start_at, thumbnail, title) "
                        + " values (?, ?, 'RANKING', '2023-08-31 18:00', 4, 10,"
                        + " '2023-08-01 10:00', 'url', ?) ";

                Query query = em.createNativeQuery(sql);
                query.setParameter(1, (i + 1));
                query.setParameter(2, content[i]);
                query.setParameter(3, title[i]);
                query.executeUpdate();
            }
            //자율
            for (int i = 5; i < 10; i++) {
                String sql =
                    "insert into random_dance (random_dance_id, content, dance_type,"
                        + " end_at, user_id, max_user, start_at, thumbnail, title) "
                        + " values (?, ?, 'BASIC', '2023-08-31 18:00', 5, 15,"
                        + " '2023-08-20 10:00', 'url', ?) ";

                Query query = em.createNativeQuery(sql);
                query.setParameter(1, (i + 1));
                query.setParameter(2, content[i]);
                query.setParameter(3, title[i]);
                query.executeUpdate();
            }
        }

        public void makeReservation() {
            for (int i = 2; i <= 8; i++) {
                String sql = "insert into reservation (RESERVATION_ID, RANDOM_DANCE_ID, USER_ID) "
                    + " values (?, 5, " + i + ")";
                Query query = em.createNativeQuery(sql);
                query.setParameter(1, (i - 1));
                query.executeUpdate();
            }
        }

        public void makeAttend() {
            for (int i = 2; i <= 8; i++) {
                String sql =
                    "insert into attend_history (ATTEND_HISTORY_ID, RANDOM_DANCE_ID, USER_ID) "
                        + " values (?, 1, " + i + ")";
                Query query = em.createNativeQuery(sql);
                query.setParameter(1, (i - 1));
                query.executeUpdate();
            }
        }

        public void makePointHistory() {
            //개최자
            em.createNativeQuery
                ("insert into point_history (POINT_HISTORY_ID, COUNT, POINT_POLICY_ID, RANDOM_DANCE_ID, USER_ID) "
                    + " values (1, 1, 5, 1, 2)").executeUpdate();
            em.createNativeQuery
                ("insert into point_history (POINT_HISTORY_ID, COUNT, POINT_POLICY_ID, RANDOM_DANCE_ID, USER_ID) "
                    + " values (2, 1, 5, 2, 3)").executeUpdate();

            //1번 랜플댄 참여 - 각 123등
            em.createNativeQuery
                ("insert into point_history (POINT_HISTORY_ID, COUNT, POINT_POLICY_ID, RANDOM_DANCE_ID, USER_ID) "
                    + " values (3, 1, 1, 1, 3)").executeUpdate();
            em.createNativeQuery
                ("insert into point_history (POINT_HISTORY_ID, COUNT, POINT_POLICY_ID, RANDOM_DANCE_ID, USER_ID) "
                    + " values (4, 1, 2, 1, 4)").executeUpdate();
            em.createNativeQuery
                ("insert into point_history (POINT_HISTORY_ID, COUNT, POINT_POLICY_ID, RANDOM_DANCE_ID, USER_ID) "
                    + " values (5, 1, 3, 1, 5)").executeUpdate();

            //1번 랜플댄 참여 - 노래 성공 (참여자 6명+개최자 1명)
            em.createNativeQuery
                ("insert into point_history (POINT_HISTORY_ID, COUNT, POINT_POLICY_ID, RANDOM_DANCE_ID, USER_ID) "
                    + " values (6, 3, 4, 1, 3)").executeUpdate();
            em.createNativeQuery
                ("insert into point_history (POINT_HISTORY_ID, COUNT, POINT_POLICY_ID, RANDOM_DANCE_ID, USER_ID) "
                    + " values (7, 2, 4, 1, 4)").executeUpdate();
            em.createNativeQuery
                ("insert into point_history (POINT_HISTORY_ID, COUNT, POINT_POLICY_ID, RANDOM_DANCE_ID, USER_ID) "
                    + " values (8, 2, 4, 1, 5)").executeUpdate();
            em.createNativeQuery
                ("insert into point_history (POINT_HISTORY_ID, COUNT, POINT_POLICY_ID, RANDOM_DANCE_ID, USER_ID) "
                    + " values (9, 1, 4, 1, 6)").executeUpdate();
            em.createNativeQuery
                ("insert into point_history (POINT_HISTORY_ID, COUNT, POINT_POLICY_ID, RANDOM_DANCE_ID, USER_ID) "
                    + " values (10, 1, 4, 1, 7)").executeUpdate();

            //연습실 참여
            em.createNativeQuery
                ("insert into point_history (POINT_HISTORY_ID, COUNT, POINT_POLICY_ID, RANDOM_DANCE_ID, USER_ID) "
                    + " values (11, 5, 6, null, 2)").executeUpdate();
        }

        public void makeMusicApply() {
            String artist = "NEWJEANS";
            String content[] = new String[]{
                "이 곡은 가사와 멜로디가 너무 사랑스럽고 경쾌해서 연습실에 추가해주셨으면 좋겠습니다. 마음을 환기하고 기분 좋은 에너지를 얻을 수 있을 것 같아요. 제 춤 실력을 향상시키는 동기가 되는 이 노래와 함께 행복한 시간을 보내고 싶습니다."
                ,
                "이 곡은 매력적인 멜로디와 따뜻한 가사로 가득 차 있어서 노래 연습실에서 함께 부르고 싶습니다. 이 곡을 연습하면서 내면의 감정을 표현하는 법을 더욱 향상시킬 수 있을 것 같아요.'Super Shy'를 추며 자신감을 더해보고 싶습니다."
                ,
                "이 곡은 다이나믹한 사운드와 중독성 있는 리듬이 너무 좋아서 춤추고 싶습니다. 'ETA'는 저를 더욱 다채롭게 표현할 수 있는 기회가 될 것 같아요. 연습실에서 이 곡을 연습하며 더 나은 춤 실력을 향상시키고 싶습니다."
                ,
                "이 곡은 부드러운 분위기와 고요한 가사가 마음을 안정시켜줘서 연습실에 추가해주셨으면 좋겠습니다. 이 곡을 추며 감정을 더 깊게 전달하고, 보다 섬세한 표현력을 키워보고 싶습니다. 여러분과 함께 'Cool With You'를 추며 마음을 편안하게 녹일 수 있으면 좋겠어요."
                ,
                "이 곡은 활기찬 리듬과 격렬한 에너지가 느껴져서 연습실에서 함께 열정을 표현하고 싶습니다. 이 곡을 추며 에너지 넘치는 시간을 공유하며 몸과 마음을 활기차게 만들어보고 싶어요. 'Get Up'을 추면서 자유로운 분위기 속에서 춤 실력을 더욱 향상시키고 싶습니다."
            };
            String title[] = new String[]{"New Jeans", "Super Shy", "ETA", "Cool With You",
                "Get Up", "ASAP"};
            for (int i = 0; i < 5; i++) {
                String sql =
                    "insert into music_apply (MUSIC_APPLY_ID, ARTIST, CONTENT, HEART_CNT, TITLE, WRITER_ID) "
                        + " values (?, ?, ?, 1, ?, ?)";
                Query query = em.createNativeQuery(sql);
                query.setParameter(1, (i + 1));
                query.setParameter(2, artist);
                query.setParameter(3, content[i]);
                query.setParameter(4, title[i]);
                query.setParameter(5, (i + 2));
                query.executeUpdate();
            }
        }

        public void makeDanceMusic() {
            int pk = 1;
            for (int j = 1; j <= 10; j++) {
                for (int i = 1; i <= 10; i++) {
                    String sql =
                        "insert into dance_music (DANCE_MUSIC_ID, RANDOM_DANCE_ID, MUSIC_ID) "
                            + " values (?, ?, ?)";
                    Query query = em.createNativeQuery(sql);
                    query.setParameter(1, pk++);
                    query.setParameter(2, j);
                    query.setParameter(3, i);
                    query.executeUpdate();
                }
            }
        }

        public void makeHeart() {
            int pk = 1;
            for (int i = 2; i < 7; i++) {
                String sql =
                    "insert into heart (HEART_ID, MUSIC_APPLY_ID, USER_ID) "
                        + " values (?, ?, " + i + ")";
                Query query = em.createNativeQuery(sql);
                query.setParameter(1, pk++);
                query.setParameter(2, (i - 1));
                query.executeUpdate();
            }
        }
    }
}