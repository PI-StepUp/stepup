package com.pi.stepup.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.Country;
import com.pi.stepup.domain.user.dto.UserResponseDto.CountryResponseDto;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @DisplayName("국가 정보 목록 조회")
    @Test
    void readAllCountries() {
        // given
        List<Country> countryResponseDtos = makeCountries();
        doReturn(countryResponseDtos)
            .when(userRepository)
            .findAllCountries();

        // when
        List<CountryResponseDto> readCountries = userService.readAllCountries();

        // then
        assertThat(readCountries.size()).isEqualTo(countryResponseDtos.size());

        for (int i = 0; i < countryResponseDtos.size(); i++) {
            assertThat(readCountries.get(i).getCountryId()).isEqualTo(
                countryResponseDtos.get(i).getCountryId());
            assertThat(readCountries.get(i).getCountryCode()).isEqualTo(
                countryResponseDtos.get(i).getCode());
        }
    }

    private List<Country> makeCountries() {
        return new ArrayList<>(Arrays.asList(
            Country.builder().code("en").build(),
            Country.builder().code("ko").build(),
            Country.builder().code("jp").build(),
            Country.builder().code("ch").build()
        ));
    }
}