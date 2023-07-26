package com.pi.stepup.domain.rank.dao;

import com.pi.stepup.domain.rank.domain.PointHistory;
import com.pi.stepup.domain.rank.domain.PointPolicy;
import com.pi.stepup.domain.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

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
    private List<PointHistory> pointHistories;

    @Test
    @BeforeEach
    public void init() {
        makePointPolicy();
        makeUser();
        makePointHistory();
    }

    @Test
    @DisplayName("포인트 적립 내역 조회")
    public void findAllPointHistoryRepositoryTest() {
        List<PointHistory> made = makePointHistories();
        insertPointHistories(made);

        List<PointHistory> result = pointHistoryRepository.findAll();

        assertThat(result.size()).isEqualTo(made.size());
    }

    private void insertPointHistories(List<PointHistory> made) {
        made.forEach(em::persist);
    }

    private List<PointHistory> makePointHistories() {
        pointHistories = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            pointHistories.add(pointHistory);
        }
        return pointHistories;
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
                .point(0)
                .build();
    }

    private void makePointHistory() {
        pointHistory = PointHistory.builder()
                .pointPolicy(pointPolicy)
                .user(user)
                .count(0)
                .build();
    }

}