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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit1() {
            //국가
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

            //노래
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

            //포인트
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

            //랭크
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
    }
}