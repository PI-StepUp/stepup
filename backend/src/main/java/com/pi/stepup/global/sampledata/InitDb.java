package com.pi.stepup.global.sampledata;

import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.rank.constant.PointType;
import com.pi.stepup.domain.rank.constant.RankName;
import com.pi.stepup.domain.rank.domain.PointPolicy;
import com.pi.stepup.domain.rank.domain.Rank;
import com.pi.stepup.domain.user.domain.Country;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "spring.initDb.enable", havingValue = "true")
@Profile({"!danceRepoTest"})
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
            makeNotice();
            makeTalk();
            makeMeeting();
            makeComment();
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
                        + "@naver.com', 1, ?, ?, ?, 0, 'url', 1, 'refresh_token', 'ROLE_USER')";
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
                    "insert into random_dance (random_dance_id, created_at, modified_at, content, dance_type,"
                        + " end_at, user_id, max_user, start_at, thumbnail, title) "
                        + " values (?, '2023-08-01', '2023-08-01', ?, 'RANKING', '2023-08-31 18:00', 4, 10,"
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
                    "insert into random_dance (random_dance_id, created_at, modified_at, content, dance_type,"
                        + " end_at, user_id, max_user, start_at, thumbnail, title) "
                        + " values (?,'2023-08-01', '2023-08-01', ?, 'BASIC', '2023-08-31 18:00', 5, 15,"
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
                String sql = "insert into reservation (RESERVATION_ID, RANDOM_DANCE_ID, USER_ID, created_at, modified_at) "
                    + " values (?, 5, " + i + ",'2023-08-01', '2023-08-01')";
                Query query = em.createNativeQuery(sql);
                query.setParameter(1, (i - 1));
                query.executeUpdate();
            }
        }

        public void makeAttend() {
            for (int i = 2; i <= 8; i++) {
                String sql =
                    "insert into attend_history (ATTEND_HISTORY_ID, RANDOM_DANCE_ID, USER_ID, created_at, modified_at) "
                        + " values (?, 1, " + i + ",'2023-08-01', '2023-08-01')";
                Query query = em.createNativeQuery(sql);
                query.setParameter(1, (i - 1));
                query.executeUpdate();
            }
        }

        public void makePointHistory() {
            //개최자
            em.createNativeQuery
                ("insert into point_history (POINT_HISTORY_ID, COUNT, POINT_POLICY_ID, RANDOM_DANCE_ID, USER_ID, created_at, modified_at) "
                    + " values (1, 1, 5, 1, 2,'2023-08-01', '2023-08-01')").executeUpdate();
            em.createNativeQuery
                ("insert into point_history (POINT_HISTORY_ID, COUNT, POINT_POLICY_ID, RANDOM_DANCE_ID, USER_ID, created_at, modified_at) "
                    + " values (2, 1, 5, 2, 3,'2023-08-01', '2023-08-01')").executeUpdate();

            //1번 랜플댄 참여 - 각 123등
            em.createNativeQuery
                ("insert into point_history (POINT_HISTORY_ID, COUNT, POINT_POLICY_ID, RANDOM_DANCE_ID, USER_ID, created_at, modified_at) "
                    + " values (3, 1, 1, 1, 3,'2023-08-01', '2023-08-01')").executeUpdate();
            em.createNativeQuery
                ("insert into point_history (POINT_HISTORY_ID, COUNT, POINT_POLICY_ID, RANDOM_DANCE_ID, USER_ID, created_at, modified_at) "
                    + " values (4, 1, 2, 1, 4,'2023-08-01', '2023-08-01')").executeUpdate();
            em.createNativeQuery
                ("insert into point_history (POINT_HISTORY_ID, COUNT, POINT_POLICY_ID, RANDOM_DANCE_ID, USER_ID, created_at, modified_at) "
                    + " values (5, 1, 3, 1, 5,'2023-08-01', '2023-08-01')").executeUpdate();

            //1번 랜플댄 참여 - 노래 성공 (참여자 6명+개최자 1명)
            em.createNativeQuery
                ("insert into point_history (POINT_HISTORY_ID, COUNT, POINT_POLICY_ID, RANDOM_DANCE_ID, USER_ID, created_at, modified_at) "
                    + " values (6, 3, 4, 1, 3,'2023-08-01', '2023-08-01')").executeUpdate();
            em.createNativeQuery
                ("insert into point_history (POINT_HISTORY_ID, COUNT, POINT_POLICY_ID, RANDOM_DANCE_ID, USER_ID, created_at, modified_at) "
                    + " values (7, 2, 4, 1, 4,'2023-08-01', '2023-08-01')").executeUpdate();
            em.createNativeQuery
                ("insert into point_history (POINT_HISTORY_ID, COUNT, POINT_POLICY_ID, RANDOM_DANCE_ID, USER_ID, created_at, modified_at) "
                    + " values (8, 2, 4, 1, 5,'2023-08-01', '2023-08-01')").executeUpdate();
            em.createNativeQuery
                ("insert into point_history (POINT_HISTORY_ID, COUNT, POINT_POLICY_ID, RANDOM_DANCE_ID, USER_ID, created_at, modified_at) "
                    + " values (9, 1, 4, 1, 6,'2023-08-01', '2023-08-01')").executeUpdate();
            em.createNativeQuery
                ("insert into point_history (POINT_HISTORY_ID, COUNT, POINT_POLICY_ID, RANDOM_DANCE_ID, USER_ID, created_at, modified_at) "
                    + " values (10, 1, 4, 1, 7,'2023-08-01', '2023-08-01')").executeUpdate();

            //연습실 참여
            em.createNativeQuery
                ("insert into point_history (POINT_HISTORY_ID, COUNT, POINT_POLICY_ID, RANDOM_DANCE_ID, USER_ID, created_at, modified_at) "
                    + " values (11, 5, 6, null, 2,'2023-08-01', '2023-08-01')").executeUpdate();
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
                    "insert into music_apply (MUSIC_APPLY_ID, ARTIST, CONTENT, HEART_CNT, TITLE, WRITER_ID, created_at, modified_at) "
                        + " values (?, ?, ?, 1, ?, ?,'2023-08-01', '2023-08-01')";
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
                        "insert into dance_music (DANCE_MUSIC_ID, RANDOM_DANCE_ID, MUSIC_ID, created_at, modified_at) "
                            + " values (?, ?, ?,'2023-08-01', '2023-08-01')";
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
                    "insert into heart (HEART_ID, MUSIC_APPLY_ID, USER_ID, created_at, modified_at) "
                        + " values (?, ?, " + i + ",'2023-08-01', '2023-08-01')";
                Query query = em.createNativeQuery(sql);
                query.setParameter(1, pk++);
                query.setParameter(2, (i - 1));
                query.executeUpdate();
            }
        }

        public void makeNotice() {

            String[] title = new String[]{
                    "(2023-08-1) 제 1회 랜덤플레이댄스 온라인 대회 안내",
                    "연습실 기능 추가 안내",
                    "오프라인 랜덤 댄스 파티 개최 안내",
                    "댄스 연습 노래 신청 추가",
                    "(2023-08-20) 제 2회 랜덤 댄스 대회 참가 신청 안내"
            };

            String[] content = new String[]{
                    "8월 1일 제 1회 랜덤플레이댄스 온라인 대회가 개최됩니다! 많은 관심과 참여 부탁드립니다.",
                    "연습실 기능이 추가되었습니다. 이제 연습실에서 캠을 키고 댄스 연습을 할 수 있어요. 열심히 연습해서 멋진 댄서가 되어보세요!",
                    "오프라인 랜덤 댄스 파티가 오늘 개최됩니다. 다양한 음악과 함께 신나는 시간을 보내고 싶다면 지금 바로 참가 신청해주세요!",
                    "이제부터 원하는 댄스 연습 노래를 신청할 수 있는 기능이 추가되었습니다. 연습실에서 연습하고 여러분의 멋진 댄스 실력을 자랑해보세요.",
                    "8월 20일 제 2회 랜덤플레이댄스 온라인 대회가 개최됩니다. 다양한 춤 스타일로 경쟁하고 포인트를 받아보세요!"
            };

            String[] fileURL = new String[]{
                    "url1",
                    "url2",
                    "url3",
                    "",
                    "url5"
            };

            Long[] randomDanceId = new Long[]{
                    1L,
                    null,
                    null,
                    null,
                    5L
            };

            for (int i = 0; i < title.length; i++) {
                String sql = "INSERT INTO board (board_id, created_at, modified_at, writer, title, content," +
                        " file_url, board_type) VALUES (?, '2023-08-01', '2023-08-01', 1, ?, ?, ?, 'NOTICE')";

                Query query = em.createNativeQuery(sql);
                query.setParameter(1, (i + 1));
                query.setParameter(2, title[i]);
                query.setParameter(3, content[i]);
                query.setParameter(4, fileURL[i]);
                query.executeUpdate();

            }

            for (int i = 0; i < title.length; i++) {
                String sql = "INSERT INTO notice (board_id, random_dance_id) VALUES (?, ?)";

                Query query = em.createNativeQuery(sql);
                query.setParameter(1, (i + 1));
                query.setParameter(2, randomDanceId[i]);
                query.executeUpdate();
            }
        }

        public void makeTalk() {

            String[] title = new String[]{
                    "내일 성수 카페 갈 사람 모여주세요!",
                    "오늘 저녁 같이 먹을 사람 구해요",
                    "여행 같이 갈 사람 ??!",
                    "영화 보러 같이 가실 분(서현 !!)",
                    "강남 운동 같이 할 친구 구해요"
            };

            String[] content = new String[]{
                    "요즘 성수 카페 새로 생긴데 많던데 같이 갈사람 댓글 남겨주세요 ~~",
                    "오늘 저녁에 같이 먹을 사람을 구해요. 같이 식사하며 이야기 나누고 싶어요!",
                    "여행 같이 떠날 친구 구합니당. 원하시는 여행지 있으면 같이 정해봐요!",
                    "영화 보러 갈 사람을 찾아요~ 같이 영화 관람하면서 즐거운 시간 보내요. 분당쪽 입니다 ",
                    "강남에서 같이 운동 하실 분?? 서로 자세도 봐주고 건강하게 함께 운동합시다 :)"
            };

            Long[] writer = new Long[]{
                    6L,
                    3L,
                    5L,
                    4L,
                    8L
            };

            String[] fileURL = new String[]{
                    "url6",
                    "",
                    "url8",
                    "url9",
                    ""
            };

            int[] comment_cnt = new int[]{
                    3,
                    1,
                    2,
                    1,
                    2
            };


            for (int i = 0; i < title.length; i++) {
                String sql = "INSERT INTO board (board_id, created_at, modified_at, writer, title, content," +
                        " file_url, board_type) VALUES (?, '2023-08-02', '2023-08-02', ?, ?, ?, ?, 'TALK')";

                Query query = em.createNativeQuery(sql);
                query.setParameter(1, (i + 6));
                query.setParameter(2, writer[i]);
                query.setParameter(3, title[i]);
                query.setParameter(4, content[i]);
                query.setParameter(5, fileURL[i]);
                query.executeUpdate();

            }

            for (int i = 0; i < title.length; i++) {
                String sql = "INSERT INTO talk (board_id, comment_cnt) VALUES (?,?)";

                Query query = em.createNativeQuery(sql);
                query.setParameter(1, (i + 6));
                query.setParameter(2, comment_cnt[i]);
                query.executeUpdate();
            }
        }

        public void makeMeeting() {

            String[] title = new String[]{
                    "주말 댄스 모임 참여자 모집",
                    "서울 강남역 연습실 같이 빌리실 분 구해요",
                    "같이 댄스 연습할 친구 모집합니다",
                    "오프라인 댄스 파티 참가자 모집",
                    "댄스 실력 상관없이 함께 춤출 분 찾아요!"
            };

            String[] content = new String[]{
                    "주말에 서울 강남역 근처 연습실을 같이 빌려서 댄스 연습하실 분을 찾습니다. 함께 실력을 향상시키고 즐겁게 춤춰요!",
                    "강남역 근처 연습실을 예약하려고 하는데 혼자서는 비용이 부담스럽네요 ㅠㅠ 같이 연습실 빌려서 실력을 향상시킬 댄서를 모집합니다.",
                    "춤 좋아하는 분들과 함께 정기적으로 모여서 연습하고 발전하는 모임을 만들려고 합니다. 관심 있으신 분들은 연락주세요! (오픈채팅)",
                    "오프라인 댄스 파티에 참가하실 분을 모집합니다. 음악과 함께 즐거운 시간 보내고 멋진 댄서들과 인연을 만들어요! ㅋㅋㅋ",
                    "댄스 실력에 상관없이 즐겁게 춤출 수 있는 모임을 만들어요. 댄스를 좋아하고 함께 즐길 친구들을 찾습니다!"
            };

            Long[] writer = new Long[]{
                    2L,
                    3L,
                    7L,
                    5L,
                    6L
            };

            String[] fileURL = new String[]{
                    "url11",
                    "",
                    "url13",
                    "url14",
                    ""
            };

            String[] region = new String[]{
                    "서울 강남역 근처",
                    "서울 분당역 근처",
                    "인천 연수구",
                    "대구 동성로",
                    "부산 서면"
            };

            int[] comment_cnt = new int[]{
                    0,
                    3,
                    1,
                    2,
                    1
            };

            LocalDateTime now = LocalDateTime.now();

            LocalDateTime[] start_at = {
                    now.plusDays(1).plusHours(10),
                    now.plusDays(2).plusHours(14),
                    now.plusDays(3).plusHours(12),
                    now.plusDays(4).plusHours(15),
                    now.plusDays(5).plusHours(11)
            };

            LocalDateTime[] end_at = {
                    start_at[0].plusHours(3),
                    start_at[1].plusHours(2),
                    start_at[2].plusHours(4),
                    start_at[3].plusHours(3),
                    start_at[4].plusHours(2)
            };


            for (int i = 0; i < title.length; i++) {
                String sql = "INSERT INTO board (board_id, created_at, modified_at, writer, title, content," +
                        " file_url, board_type) VALUES (?, '2023-08-03', '2023-08-03', ?, ?, ?, ?, 'MEETING')";

                Query query = em.createNativeQuery(sql);
                query.setParameter(1, (i + 11));
                query.setParameter(2, writer[i]);
                query.setParameter(3, title[i]);
                query.setParameter(4, content[i]);
                query.setParameter(5, fileURL[i]);
                query.executeUpdate();

            }

            for (int i = 0; i < title.length; i++) {
                String sql = "INSERT INTO meeting (board_id, region, start_at, end_at, comment_cnt) VALUES (?,?,?,?,?)";


                Query query = em.createNativeQuery(sql);
                query.setParameter(1, (i + 11));
                query.setParameter(2, region[i]);
                query.setParameter(3, start_at[i]);
                query.setParameter(4, end_at[i]);
                query.setParameter(5, comment_cnt[i]);
                query.executeUpdate();
            }
        }

        public void makeComment() {
            String[] content = new String[]{
                    "저도 성수 카페에 관심 있어요! 같이 가면 좋을 것 같아요.",
                    "저도 서울 성동구에 사는데 성수 카페 같이가요 ! ㅋㅋㅋ",
                    "오늘은 시간이 안되서 아쉽네요ㅠㅠ 다음 기회에는 꼭 함께하고 싶어요!",
                    "오늘 저녁 시간이 맞으면 함께 먹고 싶은데 장소는 어디인가요?",
                    "몇 명 생각하고 계세요? 같이 가는 사람들과 같이 아이디어를 공유하면 좋을 것 같아요!",
                    "저도 여행 같이 갈 친구를 찾고 있는데 ! 혹시 어디 생각하세요 ??",
                    "영화 새로 나온거 보고싶었는데 저는 양재라 중간 위치도 괜찮으세요?",
                    "저도 강남에서 운동 다니고 있는데 같이 운동을 하면 좋을 거 같네요 ㅋㅋ",
                    "무게 몇 정도 치세요?",
                    "오 저도 비용이 부담스러웠는데 같이 연습하면 부담없고 더 재밌을거 같네용!",
                    "강남역 근처인데, 위치가 좋네요! 함께 참가하고 싶어요~",
                    "강남역 근처 연습실 좋네요. 저도 같이 할래요!!!!!",
                    "저도 춤 좋아해서 관심이 있어요. 카카오톡 아이디로 연락 주세요!",
                    "파티에 참가하고 싶어요. 어떻게 신청하면 되나요?",
                    "댄스 파티 너무 기대돼요 ㅎㅎㅎ",
                    "저도 참여하고 싶긴한데 조금 부족해도 괜찮나요?"
            };

            Long[] board_id = new Long[]{
                    6l,
                    6l,
                    6L,
                    7L,
                    8L,
                    8L,
                    9L,
                    10l,
                    10l,
                    12l,
                    12L,
                    12L,
                    13L,
                    14L,
                    14L,
                    15L
            };

            Long[] user_id = new Long[]{
                    2l,
                    7l,
                    8L,
                    2L,
                    3L,
                    4L,
                    5L,
                    6l,
                    7l,
                    8l,
                    4L,
                    5L,
                    2L,
                    4L,
                    6L,
                    7L
            };

            for (int i = 0; i < content.length; i++) {
                String sql = "INSERT INTO comment (comment_id,  created_at, modified_at, user_id,  content, board_id) " +
                        "VALUES (?,'2023-08-02', '2023-08-02', ?, ?,?)";

                Query query = em.createNativeQuery(sql);
                query.setParameter(1, (i + 1));
                query.setParameter(2, user_id[i]);
                query.setParameter(3, content[i]);
                query.setParameter(4, board_id[i]);
                query.executeUpdate();
            }
        }
    }
}