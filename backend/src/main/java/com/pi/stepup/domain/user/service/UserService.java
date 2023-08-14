package com.pi.stepup.domain.user.service;

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
import java.util.List;

public interface UserService {

    List<CountryResponseDto> readAllCountries();

    void checkEmailDuplicated(CheckEmailRequestDto checkEmailRequestDto);

    void checkNicknameDuplicated(CheckNicknameRequestDto checkNicknameRequestDto);

    void checkIdDuplicated(CheckIdRequestDto checkIdRequestDto);

    AuthenticatedResponseDto login(LoginRequestDto loginRequestDto);

    AuthenticatedResponseDto signUp(SignUpRequestDto signUpRequestDto);

    UserInfoResponseDto readOne();

    void delete();

    void checkPassword(CheckPasswordRequestDto checkPasswordRequestDto);

    void update(UpdateUserRequestDto updateUserRequestDto);

    void findId(FindIdRequestDto findIdRequestDto);

    void findPassword(FindPasswordRequestDto findPasswordRequestDto);
}
