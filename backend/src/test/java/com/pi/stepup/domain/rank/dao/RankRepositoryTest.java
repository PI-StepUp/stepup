package com.pi.stepup.domain.rank.dao;

import com.pi.stepup.domain.dance.constant.DanceType;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.rank.constant.RankName;
import com.pi.stepup.domain.rank.domain.PointHistory;
import com.pi.stepup.domain.rank.domain.PointPolicy;
import com.pi.stepup.domain.rank.domain.Rank;
import com.pi.stepup.domain.rank.domain.UserRank;
import com.pi.stepup.domain.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static com.pi.stepup.domain.rank.constant.PointType.FIRST_PRIZE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
class RankRepositoryTest {
    @PersistenceContext
    EntityManager em;

    @Autowired
    RankRepository rankRepository;

    private User user;
    private UserRank userRank;
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
        makeUserRank();
        makeRank();
    }

    /**
     * TODO :
     *  1. 유저 포인트 조회
     *  2. 포인트 범위에 따라 랭크 조정 <- 이 부분을 포인트 업데이트 할 때마다 바꿔줘야 할 듯
     *  3. 랭크 조회
     */
    @Test
    @DisplayName("등급 조회 테스트")
    public void findRankRepositoryTest() {
        Optional<UserRank> result = rankRepository.findByUserId(user.getId());

        UserRank resultUserRank = null;
        if (result.isPresent()) {
            resultUserRank = result.get();
        }
        System.out.println(user);
        assertThat(resultUserRank).isEqualTo(user.getUserRank());
    }


    private void makeRank() {
        rank = Rank.builder()
                .name(RankName.BRONZE)
                .startPoint(0)
                .endPoint(99)
                .build();
        em.persist(rank);
    }

    private void makeUserRank() {
        userRank = UserRank.builder()
                .rank(rank)
                .user(user)
                .build();
        userRank.setUserAndSetThis(user);
        em.persist(userRank);
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
                .userRank(userRank)
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