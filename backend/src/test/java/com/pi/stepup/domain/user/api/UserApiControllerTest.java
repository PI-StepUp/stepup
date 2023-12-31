package com.pi.stepup.domain.user.api;

import static com.pi.stepup.domain.user.api.UserApiUrls.CHECK_EMAIL_DUPLICATED_URL;
import static com.pi.stepup.domain.user.api.UserApiUrls.CHECK_ID_DUPLICATED_URL;
import static com.pi.stepup.domain.user.api.UserApiUrls.CHECK_NICKNAME_DUPLICATED_URL;
import static com.pi.stepup.domain.user.api.UserApiUrls.CHECK_PASSWORD_URL;
import static com.pi.stepup.domain.user.api.UserApiUrls.DELETE_URL;
import static com.pi.stepup.domain.user.api.UserApiUrls.FIND_ID_URL;
import static com.pi.stepup.domain.user.api.UserApiUrls.FIND_PASSWORD_URL;
import static com.pi.stepup.domain.user.api.UserApiUrls.LOGIN_URL;
import static com.pi.stepup.domain.user.api.UserApiUrls.READ_ALL_COUNTRIES_URL;
import static com.pi.stepup.domain.user.api.UserApiUrls.READ_ONE_URL;
import static com.pi.stepup.domain.user.api.UserApiUrls.SIGN_UP_URL;
import static com.pi.stepup.domain.user.api.UserApiUrls.UPDATE_URL;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.CHECK_EMAIL_DUPLICATED_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.CHECK_ID_DUPLICATED_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.CHECK_NICKNAME_DUPLICATED_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.CHECK_PASSWORD_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.DELETE_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.FIND_ID_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.FIND_PASSWORD_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.LOGIN_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.READ_ALL_COUNTRIES_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.READ_ONE_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.SIGN_UP_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.UPDATE_USER_SUCCESS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.pi.stepup.domain.rank.constant.RankName;
import com.pi.stepup.domain.rank.domain.Rank;
import com.pi.stepup.domain.user.constant.UserRole;
import com.pi.stepup.domain.user.domain.Country;
import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.domain.user.dto.TokenInfo;
import com.pi.stepup.domain.user.dto.UserRequestDto.CheckEmailRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.CheckIdRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.CheckNicknameRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.CheckPasswordRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.FindIdRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.FindPasswordRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.LoginRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.SignUpRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.UpdateUserRequestDto;
import com.pi.stepup.domain.user.dto.UserResponseDto.AuthenticatedResponseDto;
import com.pi.stepup.domain.user.dto.UserResponseDto.CountryResponseDto;
import com.pi.stepup.domain.user.dto.UserResponseDto.UserInfoResponseDto;
import com.pi.stepup.domain.user.exception.EmailDuplicatedException;
import com.pi.stepup.domain.user.exception.IdDuplicatedException;
import com.pi.stepup.domain.user.exception.NicknameDuplicatedException;
import com.pi.stepup.domain.user.exception.UserNotFoundException;
import com.pi.stepup.domain.user.service.UserRedisService;
import com.pi.stepup.domain.user.service.UserService;
import com.pi.stepup.domain.user.util.WithMockCustomUser;
import com.pi.stepup.global.config.security.SecurityConfig;
import com.pi.stepup.global.util.jwt.JwtTokenProvider;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@Import(SecurityConfig.class)
@WebMvcTest(UserApiController.class)
class UserApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private UserRedisService userRedisService;

    private final Gson gson = new Gson();
    private final String UTF_8 = "UTF-8";

    private final String[] testCountryCodes = new String[]{"en", "ko", "jp", "ch"};
    private final String TEST_EMAIL = "test@test.com";
    private final String TEST_ID = "testId";
    private final String TEST_NICKNAME = "testNickname";

    @DisplayName("조회에 성공할 경우 국가 정보와 성공 상태 및 메세지가 반환된다.")
    @Test
    void readAllCountries() throws Exception {
        // given
        when(userService.readAllCountries()).thenReturn(makeCountryResponseDtos());

        // when, then
        mockMvc.perform(get(READ_ALL_COUNTRIES_URL.getUrl()))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("message").value(READ_ALL_COUNTRIES_SUCCESS.getMessage()))
            .andExpect(jsonPath("data").isArray())
            .andExpect(jsonPath("data[2].countryId").value(2))
            .andExpect(jsonPath("data[2].countryCode").value(testCountryCodes[2]));

    }

    @DisplayName("service의 checkEmailDuplicated 메서드가 호출되며 중복이 아닐 경우 성공 상태 및 메세지가 반환된다.")
    @Test
    void checkEmailDuplicated_NoDuplicated() throws Exception {
        // given
        doNothing().when(userService).checkEmailDuplicated(any(CheckEmailRequestDto.class));

        String content = makeJsonDuplicatedRequestDto(CheckEmailRequestDto.class, TEST_EMAIL);

        // when, then
        mockMvc.perform(
                post(CHECK_EMAIL_DUPLICATED_URL.getUrl())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
                    .characterEncoding(UTF_8)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("message").value(CHECK_EMAIL_DUPLICATED_SUCCESS.getMessage()));

        verify(userService, times(1)).checkEmailDuplicated(any(CheckEmailRequestDto.class));
    }

    @DisplayName("service의 checkEmailDuplicated 메서드가 호출되며 중복일 경우 충돌 상태가 반환된다.")
    @Test
    void checkEmailDuplicated_Duplicated() throws Exception {
        // given
        doThrow(EmailDuplicatedException.class)
            .when(userService)
            .checkEmailDuplicated(any(CheckEmailRequestDto.class));

        String content = makeJsonDuplicatedRequestDto(CheckEmailRequestDto.class, TEST_EMAIL);

        // when, then
        mockMvc.perform(
                post(CHECK_EMAIL_DUPLICATED_URL.getUrl())
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(UTF_8)
                    .content(content)
            )
            .andExpect(status().isConflict());

        verify(userService, times(1)).checkEmailDuplicated(any(CheckEmailRequestDto.class));
    }

    @DisplayName("service의 checkNicknameDuplicated 메서드가 호출되며 중복이 아닐 경우 성공 상태 및 메세지가 반환된다.")
    @Test
    void checkNicknameDuplicatedTest_NoDuplicated() throws Exception {
        // given
        doNothing().when(userService)
            .checkNicknameDuplicated(any(CheckNicknameRequestDto.class));

        String content = makeJsonDuplicatedRequestDto(CheckNicknameRequestDto.class, TEST_NICKNAME);

        // when, then
        mockMvc.perform(
                post(CHECK_NICKNAME_DUPLICATED_URL.getUrl())
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(UTF_8)
                    .content(content)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("message").value(CHECK_NICKNAME_DUPLICATED_SUCCESS.getMessage()));

        verify(userService, times(1)).checkNicknameDuplicated(any(CheckNicknameRequestDto.class));
    }

    @DisplayName("service의 checkNicknameDuplicated 메서드가 호출되며 중복일 경우 충돌 상태가 반환된다.")
    @Test
    void checkNicknameDuplicated_Duplicated() throws Exception {
        // given
        doThrow(NicknameDuplicatedException.class)
            .when(userService).checkNicknameDuplicated(any(CheckNicknameRequestDto.class));

        String content = makeJsonDuplicatedRequestDto(CheckNicknameRequestDto.class, TEST_NICKNAME);

        // when, then
        mockMvc.perform(
                post(CHECK_NICKNAME_DUPLICATED_URL.getUrl())
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(UTF_8)
                    .content(content)
            )
            .andExpect(status().isConflict());

        verify(userService, times(1)).checkNicknameDuplicated(any(CheckNicknameRequestDto.class));
    }

    @DisplayName("service의 checkIdDuplicated 메서드가 호출되며 중복이 아닐 경우 성공 상태 및 메세지가 반환된다.")
    @Test
    void checkIdDuplicatedTest_NoDuplicated() throws Exception {
        // given
        doNothing().when(userService).checkIdDuplicated(any(CheckIdRequestDto.class));
        String content = makeJsonDuplicatedRequestDto(CheckIdRequestDto.class, TEST_ID);

        //when, then
        mockMvc.perform(
                post(CHECK_ID_DUPLICATED_URL.getUrl())
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(UTF_8)
                    .content(content)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("message").value(CHECK_ID_DUPLICATED_SUCCESS.getMessage()));

        verify(userService, times(1)).checkIdDuplicated(any(CheckIdRequestDto.class));
    }

    @DisplayName("service의 checkIdDuplicated 메서드가 호출되며 중복일 경우 충돌 상태가 반환된다.")
    @Test
    void checkIdDuplicatedTest_Duplicated() throws Exception {
        // given
        doThrow(IdDuplicatedException.class)
            .when(userService)
            .checkIdDuplicated(any(CheckIdRequestDto.class));
        String content = makeJsonDuplicatedRequestDto(CheckIdRequestDto.class, TEST_ID);

        // when, then
        mockMvc.perform(
                post(CHECK_ID_DUPLICATED_URL.getUrl())
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(UTF_8)
                    .content(content)
            )
            .andExpect(status().isConflict());

        verify(userService, times(1)).checkIdDuplicated(any(CheckIdRequestDto.class));
    }

    @DisplayName("조회에 성공할 경우 토큰 정보와 회원 정보를 반환한다.")
    @WithMockCustomUser
    @Test
    void readOneTest() throws Exception {
        User user = makeSampleUserData();

        doReturn(UserInfoResponseDto.builder()
            .user(user)
            .build()).when(userService).readOne();

        ResultActions resultActions = mockMvc.perform(get(READ_ONE_URL.getUrl()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("message").value(READ_ONE_SUCCESS.getMessage()));

        checkUserInfoResponse(resultActions, user, "data.");
    }

    @DisplayName("회원가입에 성공할 경우 토큰 정보와 회원 정보를 반환한다.")
    @Test
    void signUpTest() throws Exception {
        // given
        User user = makeSampleUserData();
        TokenInfo tokenInfo = makeSampleTokenData();

        doReturn(AuthenticatedResponseDto.builder()
            .user(user)
            .tokenInfo(tokenInfo)
            .build()).when(userService).signUp(any(SignUpRequestDto.class));

        // when, then
        ResultActions resultActions = mockMvc.perform(
                post(SIGN_UP_URL.getUrl())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .characterEncoding(UTF_8)
                    .content(makeJsonSignUpRequestDto(user))
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("message").value(SIGN_UP_SUCCESS.getMessage()));

        String tokenInfoPrefix = "data.tokens.";
        String userInfoPrefix = "data.userInfo.";
        checkTokenInfoResponse(resultActions, tokenInfo, tokenInfoPrefix);
        checkUserInfoResponse(resultActions, user, userInfoPrefix);
    }

    @DisplayName("로그인에 성공할 경우 토큰 정보와 회원 정보를 반환한다.")
    @Test
    void loginTest() throws Exception {
        User user = makeSampleUserData();
        TokenInfo tokenInfo = makeSampleTokenData();

        doReturn(AuthenticatedResponseDto.builder()
            .user(user)
            .tokenInfo(tokenInfo)
            .build())
            .when(userService)
            .login(any(LoginRequestDto.class));

        ResultActions resultActions = mockMvc.perform(
                post(LOGIN_URL.getUrl())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .characterEncoding(UTF_8)
                    .content(gson.toJson(
                        LoginRequestDto.builder()
                            .id(user.getId())
                            .password(user.getPassword())
                            .build()
                    ))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("message").value(LOGIN_SUCCESS.getMessage()));

        String tokenInfoPrefix = "data.tokens.";
        String userInfoPrefix = "data.userInfo.";
        checkTokenInfoResponse(resultActions, tokenInfo, tokenInfoPrefix);
        checkUserInfoResponse(resultActions, user, userInfoPrefix);
    }

    @DisplayName("아이디 찾기에 성공할 경우 service의 findId 메서드가 호출되며 성공 상태 및 메세지가 반환된다.")
    @Test
    void findIdTest() throws Exception {
        doNothing()
            .when(userService)
            .findId(any(FindIdRequestDto.class));

        mockMvc.perform(
                post(FIND_ID_URL.getUrl())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .characterEncoding(UTF_8)
                    .content(gson.toJson(
                        FindIdRequestDto.builder().build()
                    ))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("message").value(FIND_ID_SUCCESS.getMessage()));

        verify(userService, times(1)).findId(any(FindIdRequestDto.class));
    }

    @DisplayName("비밀번호 찾기에 성공할 경우 service의 findPassword 메서드가 호출되며 성공 상태 및 메세지가 반환된다.")
    @Test
    void findPasswordTest() throws Exception {
        doNothing()
            .when(userService)
            .findPassword(any(FindPasswordRequestDto.class));

        mockMvc.perform(
                post(FIND_PASSWORD_URL.getUrl())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .characterEncoding(UTF_8)
                    .content(gson.toJson(
                        FindPasswordRequestDto.builder().build()
                    ))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("message").value(FIND_PASSWORD_SUCCESS.getMessage()));

        verify(userService, times(1)).findPassword(any(FindPasswordRequestDto.class));
    }

    @DisplayName("회원 탈퇴에 성공할 경우 service의 delete 메서드가 호출되며 성공 상태 및 메세지가 반환된다.")
    @WithMockUser
    @Test
    void deleteTest() throws Exception {
        doNothing()
            .when(userService)
            .delete();

        mockMvc.perform(
                delete(DELETE_URL.getUrl())
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("message").value(DELETE_SUCCESS.getMessage()));

        verify(userService, times(1)).delete();
    }

    @DisplayName("수정에 성공할 경우 성공 상태 및 메세지가 반환된다.")
    @WithMockUser
    @Test
    void updateTest() throws Exception {
        doNothing()
            .when(userService)
            .update(any(UpdateUserRequestDto.class));

        mockMvc.perform(
                put(UPDATE_URL.getUrl())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .characterEncoding(UTF_8)
                    .content(gson.toJson(UpdateUserRequestDto.builder().build()))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("message").value(UPDATE_USER_SUCCESS.getMessage()));
    }

    @DisplayName("비밀번호가 일치할 경우 성공 상태 및 메세지가 반환된다.")
    @WithMockUser
    @Test
    void checkPasswordTest_Same() throws Exception {
        doNothing()
            .when(userService)
            .checkPassword(any(CheckPasswordRequestDto.class));

        mockMvc.perform(
                post(CHECK_PASSWORD_URL.getUrl())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .characterEncoding(UTF_8)
                    .content(gson.toJson(CheckPasswordRequestDto.builder().build()))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("message").value(CHECK_PASSWORD_SUCCESS.getMessage()));
    }

    @DisplayName("비밀번호가 일치하지 않을 경우 잘못된 요청 상태가 반환된다.")
    @WithMockUser
    @Test
    void checkPasswordTest_NotSame() throws Exception {
        doThrow(UserNotFoundException.class)
            .when(userService)
            .checkPassword(any(CheckPasswordRequestDto.class));

        mockMvc.perform(
                post(CHECK_PASSWORD_URL.getUrl())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .characterEncoding(UTF_8)
                    .content(gson.toJson(CheckPasswordRequestDto.builder().build()))
            )
            .andExpect(status().isBadRequest());
    }

    private ResultActions checkTokenInfoResponse(ResultActions resultActions, TokenInfo tokenInfo,
        String prefix)
        throws Exception {
        return resultActions
            .andExpect(jsonPath(prefix + "grantType").value(tokenInfo.getGrantType()))
            .andExpect(jsonPath(prefix + "accessToken").value(tokenInfo.getAccessToken()))
            .andExpect(jsonPath(prefix + "refreshToken").value(tokenInfo.getRefreshToken()));
    }

    private ResultActions checkUserInfoResponse(ResultActions resultActions, User user,
        String prefix)
        throws Exception {
        return resultActions
            .andExpect(jsonPath(prefix + "email").value(user.getEmail()))
            .andExpect(jsonPath(prefix + "emailAlert").value(user.getEmailAlert()))
            .andExpect(jsonPath(prefix + "countryId").value(user.getCountry().getCountryId()))
            .andExpect(jsonPath(prefix + "countryCode").value(user.getCountry().getCode()))
            .andExpect(jsonPath(prefix + "nickname").value(user.getNickname()))
            .andExpect(jsonPath(prefix + "birth").value(user.getBirth().toString()))
            .andExpect(jsonPath(prefix + "profileImg").value(user.getProfileImg()))
            .andExpect(jsonPath(prefix + "point").value(user.getPoint()))
            .andExpect(jsonPath(prefix + "rankName").value(user.getRank().getName().name()))
            .andExpect(jsonPath(prefix + "rankImg").value(user.getRank().getRankImg()));
    }

    private TokenInfo makeSampleTokenData() {
        String grantType = "Bearer";
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";

        return TokenInfo.builder()
            .grantType(grantType)
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    private User makeSampleUserData() {
        String password = "userPassword";
        int emailAlert = 1;
        Long countryId = 1L;
        String countryCode = "ko";
        LocalDate birth = LocalDate.now();
        String profileImg = "";
        int point = 0;
        String rankImg = "rankImg";

        return User.builder()
            .id(TEST_ID)
            .password(password)
            .email(TEST_EMAIL)
            .emailAlert(emailAlert)
            .country(Country.builder().countryId(countryId).code(countryCode).build())
            .nickname(TEST_NICKNAME)
            .birth(birth)
            .profileImg(profileImg)
            .point(point)
            .rank(Rank.builder().name(RankName.BRONZE).rankImg(rankImg).build())
            .role(UserRole.ROLE_USER)
            .build();
    }

    private String makeJsonDuplicatedRequestDto(Class<?> dtoType, String param) {

        if (dtoType == CheckEmailRequestDto.class) {
            return gson.toJson(
                CheckEmailRequestDto.builder()
                    .email(param).build()
            );
        }

        if (dtoType == CheckNicknameRequestDto.class) {
            return gson.toJson(
                CheckNicknameRequestDto.builder()
                    .nickname(param).build()
            );
        }

        if (dtoType == CheckIdRequestDto.class) {
            return gson.toJson(
                CheckIdRequestDto.builder()
                    .id(TEST_ID).build()
            );
        }

        return "";
    }

    private String makeJsonSignUpRequestDto(User user) {
        return gson.toJson(SignUpRequestDto.builder()
            .id(user.getId())
            .password(user.getPassword())
            .email(user.getEmail())
            .emailAlert(user.getEmailAlert())
            .countryId(user.getCountry().getCountryId())
            .nickname(user.getNickname())
            .birth(user.getBirth().toString())
            .profileImg(user.getProfileImg())
            .role(user.getRole())
            .build());
    }

    private List<CountryResponseDto> makeCountryResponseDtos() {
        List<CountryResponseDto> countryResponseDtos = new ArrayList<>();

        for (long i = 0; i < testCountryCodes.length; i++) {
            countryResponseDtos.add(
                CountryResponseDto.builder().country(
                    Country.builder().countryId(i).code(testCountryCodes[(int) i]).build()
                ).build()
            );
        }

        return countryResponseDtos;
    }
}