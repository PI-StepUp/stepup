package com.pi.stepup.domain.user.api;

import static com.pi.stepup.domain.user.api.UserApiUrls.CHECK_EMAIL_DUPLICATED_URL;
import static com.pi.stepup.domain.user.api.UserApiUrls.CHECK_ID_DUPLICATED_URL;
import static com.pi.stepup.domain.user.api.UserApiUrls.CHECK_NICKNAME_DUPLICATED_URL;
import static com.pi.stepup.domain.user.api.UserApiUrls.READ_ALL_COUNTRIES_URL;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.CHECK_EMAIL_DUPLICATED_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.CHECK_ID_DUPLICATED_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.CHECK_NICKNAME_DUPLICATED_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.READ_ALL_COUNTRIES_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.READ_ONE_SUCCESS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.pi.stepup.domain.rank.constant.RankName;
import com.pi.stepup.domain.rank.domain.Rank;
import com.pi.stepup.domain.user.domain.Country;
import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.domain.user.dto.UserRequestDto.CheckEmailRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.CheckIdRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.CheckNicknameRequestDto;
import com.pi.stepup.domain.user.dto.UserResponseDto.CountryResponseDto;
import com.pi.stepup.domain.user.dto.UserResponseDto.UserInfoResponseDto;
import com.pi.stepup.domain.user.exception.EmailDuplicatedException;
import com.pi.stepup.domain.user.exception.IdDuplicatedException;
import com.pi.stepup.domain.user.exception.NicknameDuplicatedException;
import com.pi.stepup.domain.user.service.UserService;
import com.pi.stepup.domain.user.util.WithMockCustomUser;
import com.pi.stepup.global.config.security.SecurityConfig;
import com.pi.stepup.global.util.jwt.JwtTokenProvider;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@Import(SecurityConfig.class)
@WebMvcTest(UserApiController.class)
class UserApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private final Gson gson = new Gson();
    private final String UTF_8 = "UTF-8";

    private final String[] testCountryCodes = new String[]{"en", "ko", "jp", "ch"};
    private final String TEST_EMAIL = "test@test.com";
    private final String TEST_ID = "testId";
    private final String TEST_NICKNAME = "testNickname";

    @DisplayName("국가 정보 목록 조회 api 테스트")
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

    @DisplayName("이메일 중복 검사 테스트 - 중복 아님")
    @Test
    void checkEmailDuplicated_NoDuplicated() throws Exception {
        // given
        doNothing().when(userService).checkEmailDuplicated(any(CheckEmailRequestDto.class));

        String content = makeJsonRequestDto(CheckEmailRequestDto.class, TEST_EMAIL);

        // when, then
        mockMvc.perform(
                post(CHECK_EMAIL_DUPLICATED_URL.getUrl())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
                    .characterEncoding(UTF_8)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("message").value(CHECK_EMAIL_DUPLICATED_SUCCESS.getMessage()));
    }

    @DisplayName("이메일 중복 검사 테스트 - 중복")
    @Test
    void checkEmailDuplicated_Duplicated() throws Exception {
        // given
        doThrow(EmailDuplicatedException.class)
            .when(userService)
            .checkEmailDuplicated(any(CheckEmailRequestDto.class));

        String content = makeJsonRequestDto(CheckEmailRequestDto.class, TEST_EMAIL);

        // when, then
        mockMvc.perform(
                post(CHECK_EMAIL_DUPLICATED_URL.getUrl())
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(UTF_8)
                    .content(content)
            )
            .andExpect(status().isConflict());
    }

    @DisplayName("닉네임 중복 검사 테스트 - 중복 아님")
    @Test
    void checkNicknameDuplicatedTest_NoDuplicated() throws Exception {
        // given
        doNothing().when(userService)
            .checkNicknameDuplicated(any(CheckNicknameRequestDto.class));

        String content = makeJsonRequestDto(CheckNicknameRequestDto.class, TEST_NICKNAME);

        // when, then
        mockMvc.perform(
                post(CHECK_NICKNAME_DUPLICATED_URL.getUrl())
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(UTF_8)
                    .content(content)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("message").value(CHECK_NICKNAME_DUPLICATED_SUCCESS.getMessage()));
    }

    @DisplayName("닉네임 중복 검사 테스트 - 중복")
    @Test
    void checkNicknameDuplicated_Duplicated() throws Exception {
        // given
        doThrow(NicknameDuplicatedException.class)
            .when(userService).checkNicknameDuplicated(any(CheckNicknameRequestDto.class));

        String content = makeJsonRequestDto(CheckNicknameRequestDto.class, TEST_NICKNAME);

        // when, then
        mockMvc.perform(
                post(CHECK_NICKNAME_DUPLICATED_URL.getUrl())
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(UTF_8)
                    .content(content)
            )
            .andExpect(status().isConflict());
    }

    @DisplayName("아이디 중복 검사 테스트 - 중복 아님")
    @Test
    void checkIdDuplicatedTest_NoDuplicated() throws Exception {
        // given
        doNothing().when(userService).checkIdDuplicated(any(CheckIdRequestDto.class));
        String content = makeJsonRequestDto(CheckIdRequestDto.class, TEST_ID);

        //when, then
        mockMvc.perform(
                post(CHECK_ID_DUPLICATED_URL.getUrl())
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(UTF_8)
                    .content(content)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("message").value(CHECK_ID_DUPLICATED_SUCCESS.getMessage()));
    }

    @DisplayName("아이디 중복 검사 테스트 - 중복")
    @Test
    void checkIdDuplicatedTest_Duplicated() throws Exception {
        // given
        doThrow(IdDuplicatedException.class)
            .when(userService)
            .checkIdDuplicated(any(CheckIdRequestDto.class));
        String content = makeJsonRequestDto(CheckIdRequestDto.class, TEST_ID);

        // when, then
        mockMvc.perform(
                post(CHECK_ID_DUPLICATED_URL.getUrl())
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(UTF_8)
                    .content(content)
            )
            .andExpect(status().isConflict());
    }

    @DisplayName("회원정보 조회")
    @WithMockCustomUser
    @Test
    void readOneTest() throws Exception {
        User user = User.builder()
            .id(TEST_ID)
            .email(TEST_EMAIL)
            .nickname(TEST_NICKNAME)
            .country(Country.builder().code("ko").build())
            .rank(Rank.builder().name(RankName.BRONZE).build())
            .build();

        doReturn(UserInfoResponseDto.builder()
            .user(user)
            .build()).when(userService).readOne();

        mockMvc.perform(get("/api/user"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("message").value(READ_ONE_SUCCESS.getMessage()))
            .andExpect(jsonPath("data.email").value(user.getEmail()))
            .andExpect(jsonPath("data.nickname").value(user.getNickname()))
            .andExpect(jsonPath("data.countryCode").value(user.getCountry().getCode()))
            .andExpect(jsonPath("data.rankName").value(user.getRank().getName().name()));
    }

    private String makeJsonRequestDto(Class<?> dtoType, String param) {

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