package com.pi.stepup.domain.rank.api;

import static com.pi.stepup.domain.rank.constant.PointType.FIRST_PRIZE;
import static com.pi.stepup.domain.rank.constant.RankExceptionMessage.UNAUTHORIZED_USER_ACCESS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.pi.stepup.domain.dance.constant.DanceType;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.rank.domain.PointHistory;
import com.pi.stepup.domain.rank.domain.PointPolicy;
import com.pi.stepup.domain.rank.dto.RankRequestDto.PointUpdateRequestDto;
import com.pi.stepup.domain.rank.exception.UnauthorizedUserAccessException;
import com.pi.stepup.domain.rank.service.PointHistoryService;
import com.pi.stepup.domain.rank.service.RankService;
import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.global.util.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(RankApiController.class)
class RankApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private PointHistoryService pointHistoryService;

    @MockBean
    private RankService rankService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private Gson gson;
    private PointHistory pointHistory;
    private User user;
    private PointPolicy pointPolicy;
    private PointUpdateRequestDto pointUpdateRequestDto;
    private RandomDance randomDance;

    RankApiControllerTest() {
    }

    @Test
    @BeforeEach
    public void init() {
        gson = new Gson();
        makePointPolicy();
        makeUser();
        makePointHistory();
        makePointUpdateReqeustDto();
    }

    @Test
    @DisplayName("포인트 적립 api 테스트")
    @WithMockUser
    public void pointUpdateApiTest() throws Exception {
        String url = "/api/rank/point";

        final ResultActions postAction = mockMvc.perform(
            MockMvcRequestBuilders.post(url).with(csrf())
                .content(gson.toJson(pointUpdateRequestDto))
                .contentType(MediaType.APPLICATION_JSON)
        );

        postAction.andExpect(status().isOk());
    }

    @Test
    @DisplayName("비인증 유저 포인트 적립 예외 테스트")
    @WithAnonymousUser
    public void notLoginUserPointUpdateApiTest() throws Exception {
        String url = "/api/rank/point";

        final ResultActions postAction = mockMvc.perform(
            MockMvcRequestBuilders.post(url).with(csrf())
                .content(gson.toJson(pointUpdateRequestDto))
                .contentType(MediaType.APPLICATION_JSON)
        );

        postAction.andExpect(status().isUnauthorized());
    }

    // TODO : 포인트 적립 - 비인가된 사용자 접근 (403) 예외 처리
    @Test
    @DisplayName("비인가 사용자 포인트 적립 예외 테스트")
    @WithMockUser
    public void notAuthorizedUserPointUpdateApiTest() throws Exception {
        String url = "/api/rank/point";
        // TODO : service에서 throw 해줄 것
        doThrow(new UnauthorizedUserAccessException(UNAUTHORIZED_USER_ACCESS.getMessage()))
            .when(pointHistoryService)
            .update(any());

        final ResultActions postAction = mockMvc.perform(
            MockMvcRequestBuilders.post(url).with(csrf())
                .content(gson.toJson(pointUpdateRequestDto))
                .contentType(MediaType.APPLICATION_JSON)
        );

        postAction.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("포인트 적립 내역 조회 api 테스트")
    public void readAllPointHistoryApiTest() throws Exception {
        String url = "/api/rank/my/history/" + user.getId();

        final ResultActions getAction = mockMvc.perform(
            MockMvcRequestBuilders.get(url)
        );

        getAction.andExpect(status().isOk()).andDo(print());
    }

    @Test
    @DisplayName("포인트 조회 api 테스트")
    @WithMockUser
    public void readPointApiTest() throws Exception {
        String url = "/api/rank/my/point/" + user.getId();

        final ResultActions getAction = mockMvc.perform(
            MockMvcRequestBuilders.get(url)
        );

        getAction.andExpect(status().isOk()).andDo(print());
    }

    @Test
    @DisplayName("비인증된 사용자 포인트 조회 예외 테스트")
    @WithAnonymousUser
    public void notLoginUserReadPointApiTest() throws Exception {
        String url = "/api/rank/my/point/" + user.getId();

        final ResultActions getAction = mockMvc.perform(
            MockMvcRequestBuilders.get(url)
        );

        getAction.andExpect(status().isUnauthorized()).andDo(print());
    }

    // TODO : 포인트 조회 - 비인가된 사용자 접근 (403) 예외 처리
    @Test
    @DisplayName("비인가 사용자 포인트 조회 예외 테스트")
    @WithMockUser
    public void notAuthorizedUserReadPointApiTest() throws Exception {
        String url = "/api/rank/my/point/" + user.getId();
        // TODO : service에서 throw 해줄 것
        doThrow(new UnauthorizedUserAccessException(UNAUTHORIZED_USER_ACCESS.getMessage()))
            .when(pointHistoryService)
            .readPoint(any(String.class));

        final ResultActions getAction = mockMvc.perform(
            MockMvcRequestBuilders.get(url)
        );

        getAction.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("랭크 조회 api 테스트")
    @WithMockUser
    public void readUserRankApiTest() throws Exception {
        String url = "/api/rank/my/grade/" + user.getId();

        final ResultActions getAction = mockMvc.perform(
            MockMvcRequestBuilders.get(url)
        );

        getAction.andExpect(status().isOk()).andDo(print());
    }

    @Test
    @DisplayName("비인증 사용자 랭크 조회 예외 테스트")
    @WithAnonymousUser
    public void notLoginUserReadUserRankApiTest() throws Exception {
        String url = "/api/rank/my/grade/" + user.getId();

        final ResultActions getAction = mockMvc.perform(
            MockMvcRequestBuilders.get(url)
        );

        getAction.andExpect(status().isUnauthorized());
    }

    // TODO : 403 랭크 조회 예외 테스트
    @Test
    @DisplayName("비인증 사용자 랭크 조회 예외 테스트")
    @WithMockUser
    public void notAuthorizedUserReadUserRankApiTest() throws Exception {
        String url = "/api/rank/my/grade/" + user.getId();
        // TODO : service에서 throw 해줄 것
        doThrow(new UnauthorizedUserAccessException(UNAUTHORIZED_USER_ACCESS.getMessage()))
            .when(rankService)
            .readOne(user.getId());

        final ResultActions getAction = mockMvc.perform(
            MockMvcRequestBuilders.get(url)
        );

        getAction.andExpect(status().isForbidden());
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
            .id("user")
            .build();
    }

    private void makeRandomDance() {
        randomDance = RandomDance.builder()
            .title("title")
            .content("content")
            .danceType(DanceType.RANKING)
            .build();
    }
}