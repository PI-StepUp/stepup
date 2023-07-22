package com.pi.stepup.domain.user.dto;

import com.pi.stepup.domain.user.domain.Country;
import lombok.Builder;
import lombok.Getter;

public class UserResponseDto {

    @Getter
    public static class CountryResponseDto {

        private final Long countryId;
        private final String countryCode;

        @Builder
        private CountryResponseDto(Country country) {
            this.countryId = country.getCountryId();
            this.countryCode = country.getCode();
        }
    }
}
