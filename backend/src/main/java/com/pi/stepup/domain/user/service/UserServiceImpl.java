package com.pi.stepup.domain.user.service;

import static com.pi.stepup.domain.user.constant.UserResponseMessage.CHECK_EMAIL_DUPLICATED_FAIL;
import static com.pi.stepup.domain.user.constant.UserResponseMessage.CHECK_NICKNAME_DUPLICATED_FAIL;

import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.dto.UserRequestDto.CheckEmailRequestDto;
import com.pi.stepup.domain.user.dto.UserRequestDto.CheckNicknameRequestDto;
import com.pi.stepup.domain.user.dto.UserResponseDto.CountryResponseDto;
import com.pi.stepup.domain.user.exception.EmailDuplicatedException;
import com.pi.stepup.domain.user.exception.NicknameDuplicatedException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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
}
