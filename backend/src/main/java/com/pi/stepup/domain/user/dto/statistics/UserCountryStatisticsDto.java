package com.pi.stepup.domain.user.dto.statistics;

import lombok.Getter;

@Getter
public class UserCountryStatisticsDto {

    private final String countryCode;
    private final Long count;

    public UserCountryStatisticsDto(String countryCode, Long count) {
        this.countryCode = countryCode;
        this.count = count;
    }
}
