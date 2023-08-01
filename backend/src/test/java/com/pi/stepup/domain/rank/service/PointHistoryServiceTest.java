package com.pi.stepup.domain.rank.service;

import static com.pi.stepup.domain.rank.constant.PointType.FIRST_PRIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pi.stepup.domain.dance.constant.DanceType;
import com.pi.stepup.domain.dance.dao.DanceRepository;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.rank.constant.RankName;
import com.pi.stepup.domain.rank.dao.PointHistoryRepository;
import com.pi.stepup.domain.rank.dao.PointPolicyRepository;
import com.pi.stepup.domain.rank.dao.RankRepository;
import com.pi.stepup.domain.rank.domain.PointHistory;
import com.pi.stepup.domain.rank.domain.PointPolicy;
import com.pi.stepup.domain.rank.domain.Rank;
import com.pi.stepup.domain.rank.dto.RankRequestDto.PointUpdateRequestDto;
import com.pi.stepup.domain.rank.dto.RankResponseDto.PointHistoryFindResponseDto;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.global.config.security.SecurityUtils;
import java.util.ArrayList;
import java.util.List;
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
class PointHistoryServiceTest {

    @InjectMocks
    PointHistoryServiceImpl pointHistoryService;

    @Mock
    UserRepository userRepository;

    @Mock
    PointPolicyRepository pointPolicyRepository;

    @Mock
    PointHistoryRepository pointHistoryRepository;

    @Mock
    DanceRepository danceRepository;

    @Mock
    RankRepository rankRepository;

    private PointHistory pointHistory;
    private User user;
    private Rank rank;
    private PointPolicy pointPolicy;
    private PointUpdateRequestDto pointUpdateRequestDto;
    private RandomDance randomDance;
    private List<PointHistory> pointHistories;

    @Test
    @BeforeEach
    public void init() {
        makePointPolicy();
        makeUser();
        makePointHistory();
        makePointUpdateReqeustDto();
        makeRandomDance();
        makeRank();
    }

    @Test
    @DisplayName("포인트 적립 테스트")
    public void pointUpdateServiceTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMockedStatic = mockStatic(
            SecurityUtils.class)) {
            securityUtilsMockedStatic.when(SecurityUtils::getLoggedInUserId)
                .thenReturn(user.getId());
            when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));
            when(danceRepository.findOne(any())).thenReturn(Optional.ofNullable(randomDance));
            when(rankRepository.findOneByPoint(any())).thenReturn(Optional.ofNullable(rank));
            when(pointPolicyRepository.findOne(any())).thenReturn(Optional.ofNullable(pointPolicy));

            pointHistoryService.update(pointUpdateRequestDto);

            assertThat(user.getPoint()).isEqualTo(FIRST_PRIZE.getPoint() * 2);

            verify(pointHistoryRepository).insert(any());
        }
    }

    @Test
    @DisplayName("포인트 적립 내역 조회 테스트")
    public void readAllPointHistoryServiceTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMockedStatic = mockStatic(
            SecurityUtils.class)) {
            securityUtilsMockedStatic.when(SecurityUtils::getLoggedInUserId)
                .thenReturn(user.getId());
            makePointHistories();
            when(pointHistoryRepository.findAll(user.getId())).thenReturn(pointHistories);

            List<PointHistoryFindResponseDto> pointHistoryFindResponseDtos
                = pointHistoryService.readAll();

            assertThat(pointHistories.size()).isEqualTo(pointHistoryFindResponseDtos.size());
        }
    }

    @Test
    @DisplayName("포인트 조회 테스트")
    public void readPointServiceTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMockedStatic = mockStatic(
            SecurityUtils.class)) {
            securityUtilsMockedStatic.when(SecurityUtils::getLoggedInUserId)
                .thenReturn(user.getId());
            when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));

            int point = pointHistoryService.readPoint();

            assertThat(point).isEqualTo(user.getPoint());
        }
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
            .build();
    }

    private void makePointHistory() {
        pointHistory = PointHistory.builder().build();
    }

    private void makePointUpdateReqeustDto() {
        pointUpdateRequestDto = PointUpdateRequestDto.builder()
            .pointPolicyId(1L)
            .count(2)
            .randomDanceId(1L)
            .build();
    }

    private void makeRandomDance() {
        randomDance = RandomDance.builder()
//                .id(1L)
            .title("title")
            .content("content")
            .danceType(DanceType.RANKING)
            .build();
    }

    private List<PointHistory> makePointHistories() {
        pointHistories = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            PointHistory tmp = PointHistory.builder()
                .user(user)
                .randomDance(randomDance)
                .pointPolicy(pointPolicy)
                .count(0)
                .build();
            pointHistories.add(tmp);
        }
        return pointHistories;
    }

    private void makeRank() {
        rank = Rank.builder()
            .name(RankName.BRONZE)
            .build();
    }
}