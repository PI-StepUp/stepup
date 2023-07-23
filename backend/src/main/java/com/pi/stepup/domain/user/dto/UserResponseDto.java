package com.pi.stepup.domain.user.dto;

import com.pi.stepup.domain.user.domain.Country;
import com.pi.stepup.domain.user.domain.User;
import java.time.LocalDate;
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

    @Getter
    public static class UserInfoResponseDto {
        private final String email;
        private final Integer emailAlert;
        private final Long countryId;
        private final String countryCode;
        private final String nickname;
        private final LocalDate birth;
        private final String profileImg;
        private final Integer point;
        // TODO : Rank 정보 추가

        @Builder
        public UserInfoResponseDto(User user) {
            this.email = user.getEmail();
            this.emailAlert = user.getEmailAlert();
            this.countryId = user.getCountry().getCountryId();
            this.countryCode = user.getCountry().getCode();
            this.nickname = user.getNickname();
            this.birth = user.getBirth();
            this.profileImg = user.getProfileImg();
            this.point = user.getPoint();
        }
    }
}
