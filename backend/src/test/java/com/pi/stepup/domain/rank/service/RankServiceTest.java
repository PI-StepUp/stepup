package com.pi.stepup.domain.rank.service;

import static com.pi.stepup.domain.rank.constant.PointType.FIRST_PRIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.pi.stepup.domain.dance.constant.DanceType;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.rank.constant.RankName;
import com.pi.stepup.domain.rank.dao.RankRepository;
import com.pi.stepup.domain.rank.domain.PointHistory;
import com.pi.stepup.domain.rank.domain.PointPolicy;
import com.pi.stepup.domain.rank.domain.Rank;
import com.pi.stepup.domain.rank.dto.RankResponseDto.UserRankFindResponseDto;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.global.config.security.SecurityUtils;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RankServiceTest {

    @InjectMocks
    RankServiceImpl rankService;

    @Mock
    RankRepository rankRepository;

    @Mock
    UserRepository userRepository;

    private User user;
    private PointPolicy pointPolicy;
    private PointHistory pointHistory;
    private RandomDance randomDance;
    private Rank rank;

    @Test
    @BeforeEach
    public void init() {
        makePointPolicy();
        makeRank();
        makeUser();
        makeRandomDance();
    }

    @Test
    @DisplayName("랭크 조회 테스트")
    public void readRankServiceTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMockedStatic = mockStatic(
            SecurityUtils.class)) {
            securityUtilsMockedStatic.when(SecurityUtils::getLoggedInUserId)
                .thenReturn(user.getId());
            when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));

            UserRankFindResponseDto result = rankService.readOne();

            assertThat(result.getRankName()).isEqualTo(user.getRank().getName());
        }
    }

    private void makeRank() {
        rank = Rank.builder()
            .name(RankName.BRONZE)
            .startPoint(0)
            .endPoint(99)
            .rankImg("url")
            .build();
    }

    private void makePointPolicy() {
        pointPolicy = PointPolicy.builder()
            .pointType(FIRST_PRIZE)
            .point(FIRST_PRIZE.getPoint())
            .build();
    }

    private void makeUser() {
        user = User.builder()
            .id("user")
            .password("pass")
            .point(0)
            .rank(rank)
            .build();
    }

    private void makeRandomDance() {
        randomDance = RandomDance.builder()
            .title("title")
            .content("content").
            danceType(DanceType.RANKING)
            .build();
    }
}