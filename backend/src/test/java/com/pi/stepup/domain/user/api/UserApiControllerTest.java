package com.pi.stepup.domain.user.api;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pi.stepup.domain.user.domain.Country;
import com.pi.stepup.domain.user.dto.UserResponseDto.CountryResponseDto;
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

    @DisplayName("국가 정보 목록 조회 api 테스트")
    @Test
    void readAllCountries() throws Exception {
        // given
        when(userService.readAllCountries()).thenReturn(makeCountryResponseDtos());

        // when
        mockMvc.perform(get("/api/user/country"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("message").value("국가 코드 목록 조회 완료"))
            .andExpect(jsonPath("data").isArray())
            .andExpect(jsonPath("data[2].countryId").value(2))
            .andExpect(jsonPath("data[2].countryCode").value("jp"));

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