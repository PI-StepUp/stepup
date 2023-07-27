package com.pi.stepup.domain.rank.dao;

import static com.pi.stepup.domain.rank.constant.PointType.FIRST_PRIZE;

import com.pi.stepup.domain.dance.constant.DanceType;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.rank.constant.RankName;
import com.pi.stepup.domain.rank.domain.PointHistory;
import com.pi.stepup.domain.rank.domain.PointPolicy;
import com.pi.stepup.domain.rank.domain.Rank;
import com.pi.stepup.domain.user.domain.User;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class RankRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    RankRepository rankRepository;

    private User user;
    private PointPolicy pointPolicy;
    private PointHistory pointHistory;
    private RandomDance randomDance;
    private Rank rank;

    @Test
    @BeforeEach
    public void init() {
        makePointPolicy();
        makeUser();
        makeRandomDance();
        makeRank();
    }

    /**
     * TODO :
     *  1. 유저 포인트 조회
     *  2. 포인트 범위에 따라 랭크 조정 <- 이 부분을 포인트 업데이트 할 때마다 바꿔줘야 할 듯
     *  3. 랭크 조회
     */


    private void makeRank() {
        rank = Rank.builder()
            .name(RankName.BRONZE)
            .startPoint(0)
            .endPoint(99)
            .build();
        em.persist(rank);
    }

    private void makePointPolicy() {
        pointPolicy = PointPolicy.builder()
            .pointType(FIRST_PRIZE)
            .point(FIRST_PRIZE.getPoint())
            .build();
        em.persist(pointPolicy);
    }

    private void makeUser() {
        user = User.builder()
            .id("user")
            .password("pass")
            .point(0)
            .build();
        em.persist(user);
    }

    private void makeRandomDance() {
        randomDance = RandomDance.builder()
            .id(1L)
            .title("title")
            .content("content")
            .danceType(DanceType.RANKING)
            .build();
//        em.persist(randomDance);
    }
}