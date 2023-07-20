package com.pi.stepup.domain.user.service;

import com.pi.stepup.domain.user.dto.UserResponseDto.CountryResponseDto;
import java.util.List;

public interface UserService {

    List<CountryResponseDto> readAllCountries();

}
