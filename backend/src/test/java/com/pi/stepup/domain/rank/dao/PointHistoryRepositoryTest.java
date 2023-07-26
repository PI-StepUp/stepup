package com.pi.stepup.domain.rank.dao;

import com.pi.stepup.domain.rank.domain.PointHistory;
import com.pi.stepup.domain.rank.domain.PointPolicy;
import com.pi.stepup.domain.user.domain.User;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.pi.stepup.domain.rank.constant.PointType.FIRST_PRIZE;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PointHistoryRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    PointHistoryRepository pointHistoryRepository;

    private PointHistory pointHistory;
    private User user;
    private PointPolicy pointPolicy;

    @Test
    @BeforeEach
    public void init() {
        makePointPolicy();
        makeUser();
        makePointHistory();
    }

    @Test
    public void insertPointRepoTest() {
        User result = pointHistoryRepository.update(pointPolicy.getPoint());

        assertThat(result.getPoint()).isEqualTo(100);
    }

    private void makePointPolicy() {
        pointPolicy = PointPolicy.builder()
            .pointType(FIRST_PRIZE)
            .point(100)
            .build();
    }

    private void makeUser() {
        user = User.builder()
            .id("user")
            .password("pass")
            .build();
    }

    private void makePointHistory() {
        pointHistory = PointHistory.builder().build();
    }

}