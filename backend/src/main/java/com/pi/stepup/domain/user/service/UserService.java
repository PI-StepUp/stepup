package com.pi.stepup.domain.user.service;

import com.pi.stepup.domain.user.dto.TokenInfo;
import com.pi.stepup.domain.user.dto.UserRequestDto.CheckEmailRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.CheckIdRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.CheckNicknameRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.LoginRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.SignUpRequestDto;
import com.pi.stepup.domain.user.dto.UserResponseDto.CountryResponseDto;
import com.pi.stepup.domain.user.dto.UserResponseDto.UserInfoResponseDto;
import java.util.List;

public interface UserService {

    List<CountryResponseDto> readAllCountries();

    void checkEmailDuplicated(CheckEmailRequestDto checkEmailRequestDto);

    void checkNicknameDuplicated(CheckNicknameRequestDto checkNicknameRequestDto);

    void checkIdDuplicated(CheckIdRequestDto checkIdRequestDto);

    TokenInfo login(LoginRequestDto loginRequestDto);

    TokenInfo signUp(SignUpRequestDto signUpRequestDto);

    UserInfoResponseDto readOne(String id);

    void delete(String id);
}
