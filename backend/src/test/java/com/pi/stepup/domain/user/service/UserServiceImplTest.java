package com.pi.stepup.domain.user.service;

import static com.pi.stepup.domain.user.constant.UserResponseMessage.CHECK_EMAIL_DUPLICATED_FAIL;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.CHECK_NICKNAME_DUPLICATED_FAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.Country;
import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.domain.user.dto.UserRequestDto.CheckEmailRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.CheckNicknameRequestDto;
import com.pi.stepup.domain.user.dto.UserResponseDto.CountryResponseDto;
import com.pi.stepup.domain.user.exception.EmailDuplicatedException;
import com.pi.stepup.domain.user.exception.NicknameDuplicatedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

    private final String TEST_EMAIL = "test@test.com";
    private final String TEST_ID = "testId";
    private final String TEST_NICKNAME = "testNickname";

    @DisplayName("국가 정보 목록 조회")
    @Test
    void readAllCountriesTest() {
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

    @DisplayName("이메일 중복 검사 - 중복 아님")
    @Test
    void checkEmailDuplicatedTest_NoDuplicated() {
        // given
        when(userRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.empty());

        CheckEmailRequestDto checkEmailRequestDto = new CheckEmailRequestDto();
        checkEmailRequestDto.setEmail(TEST_EMAIL);

        // when

        // then
        assertThatNoException().isThrownBy(
            () -> userService.checkEmailDuplicated(checkEmailRequestDto));
    }

    @DisplayName("이메일 중복 검사 - 중복")
    @Test
    void checkEmailDuplicatedTest_Duplicated() {
        // given
        when(userRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(User.builder().build()));

        CheckEmailRequestDto checkEmailRequestDto = new CheckEmailRequestDto();
        checkEmailRequestDto.setEmail(TEST_EMAIL);

        // when

        // then
        assertThatThrownBy(() -> userService.checkEmailDuplicated(checkEmailRequestDto))
            .isInstanceOf(EmailDuplicatedException.class)
            .hasMessageContaining(CHECK_EMAIL_DUPLICATED_FAIL.getMessage());
    }

    @DisplayName("닉네임 중복 검사 - 중복 아님")
    @Test
    void checkNicknameDuplicatedTest_NoDuplicated() {
        // given
        when(userRepository.findByNickname(any(String.class)))
            .thenReturn(Optional.empty());

        CheckNicknameRequestDto checkNicknameRequestDto = CheckNicknameRequestDto.builder()
            .nickname(TEST_NICKNAME).build();

        // when, then
        assertThatNoException()
            .isThrownBy(() -> userService.checkNicknameDuplicated(checkNicknameRequestDto));

    }

    @DisplayName("닉네임 중복 검사 - 중복")
    @Test
    void checkNicknameDuplicatedTest_Duplicated() {
        // given
        when(userRepository.findByNickname(any(String.class)))
            .thenReturn(Optional.of(User.builder().build()));

        CheckNicknameRequestDto checkNicknameRequestDto = CheckNicknameRequestDto.builder()
            .nickname(TEST_NICKNAME).build();

        // when, then
        assertThatThrownBy(() -> userService.checkNicknameDuplicated(checkNicknameRequestDto))
            .isInstanceOf(NicknameDuplicatedException.class)
            .hasMessageContaining(CHECK_NICKNAME_DUPLICATED_FAIL.getMessage());

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