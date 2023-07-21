package com.pi.stepup.domain.user.api;

import static com.pi.stepup.domain.user.constant.UserResponseMessage.CHECK_EMAIL_DUPLICATED_SUCCESS;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.READ_ALL_COUNTRIES_SUCCESS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.pi.stepup.domain.user.domain.Country;
import com.pi.stepup.domain.user.dto.UserRequestDto.CheckEmailRequestDto;
import com.pi.stepup.domain.user.dto.UserResponseDto.CountryResponseDto;
import com.pi.stepup.domain.user.exception.EmailDuplicatedException;
import com.pi.stepup.domain.user.service.UserService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserApiController.class)
class UserApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private final String TEST_EMAIL = "test@test.com";
    private final String TEST_ID = "testId";
    private final String TEST_NICKNAME = "testNickname";

    @DisplayName("국가 정보 목록 조회 api 테스트")
    @Test
    void readAllCountries() throws Exception {
        // given
        when(userService.readAllCountries()).thenReturn(makeCountryResponseDtos());

        // when, then
        mockMvc.perform(get("/api/user/country"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("message").value(READ_ALL_COUNTRIES_SUCCESS.getMessage()))
            .andExpect(jsonPath("data").isArray())
            .andExpect(jsonPath("data[2].countryId").value(2))
            .andExpect(jsonPath("data[2].countryCode").value("jp"));

    }

    @DisplayName("이메일 중복 검사 테스트 - 중복 아님")
    @Test
    void checkEmailDuplicated_NoDuplicated() throws Exception {
        // given
        doNothing().when(userService).checkEmailDuplicated(any(CheckEmailRequestDto.class));

        String content = makeJsonCheckEmailRequest();

        // when, then
        mockMvc.perform(
                post("/api/user/dupemail")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
                    .characterEncoding("UTF-8")
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

        String content = makeJsonCheckEmailRequest();

        // when, then
        mockMvc.perform(
                post("/api/user/dupemail")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(content)
            )
            .andExpect(status().isConflict());
    }

    private String makeJsonCheckEmailRequest() {
        CheckEmailRequestDto checkEmailRequestDto = new CheckEmailRequestDto();
        checkEmailRequestDto.setEmail(TEST_EMAIL);

        Gson gson = new Gson();
        return gson.toJson(checkEmailRequestDto);
    }

    private List<CountryResponseDto> makeCountryResponseDtos() {
        return new ArrayList<>(Arrays.asList(
            CountryResponseDto.builder().country(Country.builder().countryId(0L).code("en").build())
                .build(),
            CountryResponseDto.builder().country(Country.builder().countryId(1L).code("ko").build())
                .build(),
            CountryResponseDto.builder().country(Country.builder().countryId(2L).code("jp").build())
                .build(),
            CountryResponseDto.builder().country(Country.builder().countryId(3L).code("ch").build())
                .build()
        ));
    }
}