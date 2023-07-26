package com.pi.stepup.domain.rank.service;

import com.pi.stepup.domain.dance.constant.DanceType;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.rank.constant.RankName;
import com.pi.stepup.domain.rank.dao.RankRepository;
import com.pi.stepup.domain.rank.domain.PointHistory;
import com.pi.stepup.domain.rank.domain.PointPolicy;
import com.pi.stepup.domain.rank.domain.Rank;
import com.pi.stepup.domain.rank.domain.UserRank;
import com.pi.stepup.domain.rank.dto.RankResponseDto.UserRankFindResponseDto;
import com.pi.stepup.domain.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.pi.stepup.domain.rank.constant.PointType.FIRST_PRIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RankServiceTest {
    @InjectMocks
    RankServiceImpl rankService;

    @Mock
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

    @Test
    @DisplayName("랭크 조회 테스트")
    public void readRankServiceTest() {
        // TODO : 호출 되면 user 정보 db에 set 어떻게..?
        when(rankRepository.findByUserId(any())).thenReturn(Optional.ofNullable(userRank));

        UserRankFindResponseDto result = rankService.readOne(user.getId());

        assertThat(result.getRankName()).isEqualTo(userRank.getRank().getName());
    }

    private void makeRank() {
        rank = Rank.builder()
                .name(RankName.BRONZE)
                .startPoint(0)
                .endPoint(99)
                .rankImg("url")
                .build();
    }

    private void makeUserRank() {
        userRank = UserRank.builder()
                .rank(rank)
                .user(user)
                .build();
        userRank.setUserAndSetThis(user);
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

    private void makeRandomDance() {
        randomDance = RandomDance.builder()
                .id(1L)
                .title("title")
                .content("content")
                .danceType(DanceType.RANKING)
                .build();
    }
}