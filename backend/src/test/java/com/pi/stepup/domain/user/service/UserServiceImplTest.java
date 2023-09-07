package com.pi.stepup.domain.user.service;

import static com.pi.stepup.domain.user.constant.UserExceptionMessage.EMAIL_DUPLICATED;
import static com.pi.stepup.domain.user.constant.UserExceptionMessage.ID_DUPLICATED;
import static com.pi.stepup.domain.user.constant.UserExceptionMessage.NICKNAME_DUPLICATED;
import static com.pi.stepup.domain.user.constant.UserExceptionMessage.USER_NOT_FOUND;
import static com.pi.stepup.domain.user.constant.UserExceptionMessage.WRONG_PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pi.stepup.domain.rank.domain.Rank;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.Country;
import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.domain.user.dto.UserRequestDto.CheckEmailRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.CheckIdRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.CheckNicknameRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.CheckPasswordRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.UpdateUserRequestDto;
import com.pi.stepup.domain.user.dto.UserResponseDto.CountryResponseDto;
import com.pi.stepup.domain.user.dto.UserResponseDto.UserInfoResponseDto;
import com.pi.stepup.domain.user.exception.EmailDuplicatedException;
import com.pi.stepup.domain.user.exception.IdDuplicatedException;
import com.pi.stepup.domain.user.exception.NicknameDuplicatedException;
import com.pi.stepup.domain.user.exception.UserNotFoundException;
import com.pi.stepup.global.config.security.SecurityUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Spy
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Spy
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRedisService userRedisService;

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
        try (MockedStatic<SecurityUtils> securityUtilsMocked = mockStatic(SecurityUtils.class)) {
            mockGetUserIdAndFindById(securityUtilsMocked, null);

            assertThatThrownBy(() -> userService.delete())
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(USER_NOT_FOUND.getMessage());
        }
    }

    @DisplayName("회원정보 조회에 성공할 경우 UserInfoResponseDto를 반환한다.")
    @Test
    void readOneTest() {
        // when, then
        try (MockedStatic<SecurityUtils> securityUtilsMocked = mockStatic(SecurityUtils.class)) {
            mockGetUserIdAndFindById(securityUtilsMocked, User.builder()
                .country(Country.builder().build())
                .rank(Rank.builder().build())
                .build());

            assertThat(userService.readOne()).isInstanceOf(UserInfoResponseDto.class);
        }

    }

    @DisplayName("비밀번호가 일치할 경우 예외가 발생하지 않는다.")
    @Test
    void checkPasswordTest_Same() {
        // given
        String testPassword = "testPassword";
        User user = User.builder()
            .password(passwordEncoder.encode(testPassword))
            .build();

        try (MockedStatic<SecurityUtils> securityUtilsMocked = mockStatic(SecurityUtils.class)) {
            mockGetUserIdAndFindById(securityUtilsMocked, user);

            when(passwordEncoder.matches(testPassword, user.getPassword()))
                .thenReturn(true);

            assertThatNoException().isThrownBy(() -> userService.checkPassword(
                CheckPasswordRequestDto.builder()
                    .password(testPassword)
                    .build()
            ));
        }
    }

    @DisplayName("비밀번호가 일치하지 않을 경우 예외가 발생한다.")
    @Test
    void checkPasswordTest_NotSame() {
        // given
        String testPassword = "testPassword";
        String encodedPassword = "encodePassword";

        User user = User.builder()
            .password(encodedPassword)
            .build();

        try (MockedStatic<SecurityUtils> securityUtilsMocked = mockStatic(SecurityUtils.class)) {
            mockGetUserIdAndFindById(securityUtilsMocked, user);

            when(passwordEncoder.matches(testPassword, user.getPassword()))
                .thenReturn(false);

            assertThatThrownBy(() -> userService.checkPassword(
                CheckPasswordRequestDto.builder()
                    .password(testPassword)
                    .build()
            ))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(WRONG_PASSWORD.getMessage());
        }
    }

    @DisplayName("수정 이메일이 중복될 경우 예외가 발생한다.")
    @Test
    void updateTest_EmailDuplicated() {
        // given
        String userEmail = "userEmail";
        User user = User.builder()
            .email(userEmail)
            .build();

        try (MockedStatic<SecurityUtils> securityUtilsMocked = mockStatic(SecurityUtils.class)) {
            mockGetUserIdAndFindById(securityUtilsMocked, user);

            when(userRepository.findByEmail(TEST_EMAIL))
                .thenReturn(Optional.of(User.builder().build()));

            // when, then
            assertThatThrownBy(() -> userService.update(
                UpdateUserRequestDto.builder()
                    .email(TEST_EMAIL)
                    .build()
            ))
                .isInstanceOf(EmailDuplicatedException.class)
                .hasMessageContaining(EMAIL_DUPLICATED.getMessage());
        }
    }

    @DisplayName("수정 닉네임이 중복될 경우 예외가 발생한다.")
    @Test
    void updateTest_NicknameDuplicated() {
        // given
        String userNickname = "userNickname";

        try (MockedStatic<SecurityUtils> securityUtilsMocked = mockStatic(SecurityUtils.class)) {
            mockGetUserIdAndFindById(securityUtilsMocked, User.builder()
                .email(TEST_EMAIL)
                .nickname(userNickname)
                .build());

            when(userRepository.findByNickname(TEST_NICKNAME))
                .thenReturn(Optional.of(User.builder().build()));

            assertThatThrownBy(() -> userService.update(
                UpdateUserRequestDto.builder()
                    .email(TEST_EMAIL)
                    .nickname(TEST_NICKNAME)
                    .build()
            ))
                .isInstanceOf(NicknameDuplicatedException.class)
                .hasMessageContaining(NICKNAME_DUPLICATED.getMessage());
        }
    }

    @DisplayName("기본 회원정보 수정에 성공한다")
    @Test
    void updateTest_UpdateBasicInfo() {
        // given
        UpdateUserRequestDto updateUserRequestDto = makeUpdateUserRequestDto();
        User user = makeUserSample();

        try (MockedStatic<SecurityUtils> securityUtilsMocked = mockStatic(SecurityUtils.class)) {
            mockGetUserIdAndFindById(securityUtilsMocked, user);

            when(userRepository.findByEmail(updateUserRequestDto.getEmail()))
                .thenReturn(Optional.empty());

            when(userRepository.findByNickname(updateUserRequestDto.getNickname()))
                .thenReturn(Optional.empty());

            when(userRepository.findOneCountry(updateUserRequestDto.getCountryId()))
                .thenReturn(Country.builder()
                    .countryId(updateUserRequestDto.getCountryId())
                    .code("ko")
                    .build());

            // when
            userService.update(updateUserRequestDto);

            // then
            assertUpdatedInfo(user, updateUserRequestDto);
        }
    }

    private void assertUpdatedInfo(User user, UpdateUserRequestDto updateUserRequestDto) {
        assertThat(user.getEmail()).isEqualTo(updateUserRequestDto.getEmail());
        assertThat(user.getEmailAlert()).isEqualTo(updateUserRequestDto.getEmailAlert());
        assertThat(user.getCountry().getCountryId()).isEqualTo(updateUserRequestDto.getCountryId());
        assertThat(user.getNickname()).isEqualTo(updateUserRequestDto.getNickname());
        assertThat(user.getProfileImg()).isEqualTo(updateUserRequestDto.getProfileImg());
    }

    private User makeUserSample() {
        String email = "userEmail";
        String nickname = "userNickname";
        String password = "userPassword";

        return User.builder()
            .email(email)
            .nickname(nickname)
            .password(password)
            .build();
    }

    private UpdateUserRequestDto makeUpdateUserRequestDto() {
        int testEmailAlert = 1;
        Long countryId = 1L;
        String testProfileImg = "testProfileImg";

        return UpdateUserRequestDto.builder()
            .email(TEST_EMAIL)
            .emailAlert(testEmailAlert)
            .countryId(countryId)
            .nickname(TEST_NICKNAME)
            .profileImg(testProfileImg)
            .build();
    }

    private void mockGetUserIdAndFindById(MockedStatic<SecurityUtils> securityUtilsMocked,
        User user) {
        securityUtilsMocked.when(SecurityUtils::getLoggedInUserId)
            .thenReturn(TEST_ID);

        when(userRepository.findById(TEST_ID))
            .thenReturn(Optional.ofNullable(user));
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