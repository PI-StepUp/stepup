package com.pi.stepup.domain.user.service;

import static com.pi.stepup.domain.user.constant.UserResponseMessage.CHECK_EMAIL_DUPLICATED_FAIL;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.CHECK_ID_DUPLICATED_FAIL;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.CHECK_NICKNAME_DUPLICATED_FAIL;

import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.domain.user.dto.TokenInfo;
import com.pi.stepup.domain.user.dto.UserRequestDto.CheckEmailRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.CheckIdRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.CheckNicknameRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.LoginRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.SignUpRequestDto;
import com.pi.stepup.domain.user.dto.UserResponseDto.CountryResponseDto;
import com.pi.stepup.domain.user.dto.UserResponseDto.UserInfoResponseDto;
import com.pi.stepup.domain.user.exception.EmailDuplicatedException;
import com.pi.stepup.domain.user.exception.IdDuplicatedException;
import com.pi.stepup.domain.user.exception.NicknameDuplicatedException;
import com.pi.stepup.global.util.jwt.JwtTokenProvider;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<CountryResponseDto> readAllCountries() {
        return userRepository.findAllCountries()
            .stream()
            .map(c -> CountryResponseDto.builder().country(c).build())
            .collect(Collectors.toList());
    }

    @Override
    public void checkEmailDuplicated(CheckEmailRequestDto checkEmailRequestDto) {
        if (userRepository.findByEmail(checkEmailRequestDto.getEmail()).isPresent()) {
            throw new EmailDuplicatedException(CHECK_EMAIL_DUPLICATED_FAIL.getMessage());
        }
    }

    @Override
    public void checkNicknameDuplicated(CheckNicknameRequestDto checkNicknameRequestDto) {
        if (userRepository.findByNickname(checkNicknameRequestDto.getNickname()).isPresent()) {
            throw new NicknameDuplicatedException(CHECK_NICKNAME_DUPLICATED_FAIL.getMessage());
        }
    }

    @Override
    public void checkIdDuplicated(CheckIdRequestDto checkIdRequestDto) {
        if (userRepository.findById(checkIdRequestDto.getId()).isPresent()) {
            throw new IdDuplicatedException(CHECK_ID_DUPLICATED_FAIL.getMessage());
        }
    }

    @Override
    public TokenInfo login(LoginRequestDto loginRequestDto) {
        TokenInfo tokenInfo = setFirstAuthentication(loginRequestDto.getId(),
            loginRequestDto.getPassword());
        logger.debug("login token : {}", tokenInfo);

        User user = userRepository.findById(loginRequestDto.getId()).get();
        user.setRefreshToken(tokenInfo.getRefreshToken());

        return tokenInfo;
    }

    @Override
    @Transactional
    public TokenInfo signUp(SignUpRequestDto signUpRequestDto) {
        User user = signUpRequestDto.toUser(
            passwordEncoder.encode(signUpRequestDto.getPassword()),
            userRepository.findOneCountry(signUpRequestDto.getCountryId()));

        userRepository.insert(user);

        logger.debug("user : {}", user);

        TokenInfo tokenInfo = setFirstAuthentication(signUpRequestDto.getId(),
            signUpRequestDto.getPassword());
        logger.debug("token : {}", tokenInfo);

        user.setRefreshToken(tokenInfo.getRefreshToken());

        return tokenInfo;
    }

    @Override
    public UserInfoResponseDto readOne(String id) {
        // TODO: user not found exception 설정
        return UserInfoResponseDto.builder()
            .user(userRepository.findById(id).orElseThrow())
            .build();
    }

    private TokenInfo setFirstAuthentication(String id, String password) {
        // 1. id, pw 기반 Authentication 객체 생성, 해당 객체는 인증 여부를 확인하는 authenticated 값이 false.
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(id, password);

        // 2. 검증 진행 - CustomUserDetailsService.loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject()
            .authenticate(authenticationToken);

        return jwtTokenProvider.generateToken(authentication);
    }
}
