package com.pi.stepup.domain.user.service;

import static com.pi.stepup.domain.user.constant.UserExceptionMessage.EMAIL_DUPLICATED;
import static com.pi.stepup.domain.user.constant.UserExceptionMessage.ID_DUPLICATED;
import static com.pi.stepup.domain.user.constant.UserExceptionMessage.NICKNAME_DUPLICATED;
import static com.pi.stepup.domain.user.constant.UserExceptionMessage.USER_NOT_FOUND;
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
import com.pi.stepup.domain.user.dto.UserRequestDto.CheckIdRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.CheckNicknameRequestDto;
import com.pi.stepup.domain.user.dto.UserResponseDto.CountryResponseDto;
import com.pi.stepup.domain.user.exception.EmailDuplicatedException;
import com.pi.stepup.domain.user.exception.IdDuplicatedException;
import com.pi.stepup.domain.user.exception.NicknameDuplicatedException;
import com.pi.stepup.domain.user.exception.UserNotFoundException;
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

    @DisplayName("국가 정보 목록을 조회했을 때 전체 목록 dto가 반환된다.")
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

    @DisplayName("이메일 중복 검사를 할 때 중복이 아니면 예외가 발생하지 않는다.")
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

    @DisplayName("이메일 중복 검사를 할 때 중복이면 예외가 발생한다.")
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
            .hasMessageContaining(EMAIL_DUPLICATED.getMessage());
    }

    @DisplayName("닉네임 중복 검사를 할 때 중복이 아니면 예외가 발생하지 않는다.")
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

    @DisplayName("닉네임 중복 검사를 할 때 중복이면 예외가 발생한다.")
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
            .hasMessageContaining(NICKNAME_DUPLICATED.getMessage());

    }

    @DisplayName("아이디 중복 검사를 할 때 중복이 아니면 예외가 발생하지 않는다.")
    @Test
    void checkIdDuplicatedTest_NoDuplicated() {
        // given
        when(userRepository.findById(any(String.class)))
            .thenReturn(Optional.empty());

        CheckIdRequestDto checkIdRequestDto = CheckIdRequestDto.builder()
            .id(TEST_ID).build();

        // when, then
        assertThatNoException()
            .isThrownBy(() -> userService.checkIdDuplicated(checkIdRequestDto));
    }

    @DisplayName("아이디 중복 검사를 할 때 중복이면 예외가 발생한다.")
    @Test
    void checkIdDuplicatedTest_Duplicated() {
        // given
        when(userRepository.findById(any(String.class)))
            .thenReturn(Optional.of(User.builder().build()));

        CheckIdRequestDto checkIdRequestDto = CheckIdRequestDto.builder()
            .id(TEST_ID).build();

        // then
        assertThatThrownBy(() -> userService.checkIdDuplicated(checkIdRequestDto))
            .isInstanceOf(IdDuplicatedException.class)
            .hasMessageContaining(ID_DUPLICATED.getMessage());
    }

    @DisplayName("사용자를 삭제할 때 해당 유저를 찾을 수 없을 경우 예외가 발생한다.")
    @Test
    void deleteTest_NotFound() {
        // given
        when(userRepository.findById(any(String.class))).thenReturn(Optional.empty());

        String testId = "testID";

        // when, then
        assertThatThrownBy(() -> userService.delete(testId))
            .isInstanceOf(UserNotFoundException.class)
            .hasMessageContaining(USER_NOT_FOUND.getMessage());
    }

    @DisplayName("사용자를 삭제할 때 성공할 경우 예외가 발생하지 않는다.")
    @Test
    void deleteTest_Success() {
        // given
        when(userRepository.findById(any(String.class))).thenReturn(
            Optional.of(User.builder().build()));

        String testId = "testID";

        // when, then
        assertThatNoException().isThrownBy(() -> userService.delete(testId));
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