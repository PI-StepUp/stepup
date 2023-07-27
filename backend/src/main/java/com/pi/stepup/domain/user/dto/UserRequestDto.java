package com.pi.stepup.domain.user.dto;

import com.pi.stepup.domain.user.constant.UserRole;
import com.pi.stepup.domain.user.domain.Country;
import com.pi.stepup.domain.user.domain.User;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserRequestDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckEmailRequestDto {

        private String email;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckNicknameRequestDto {

        private String nickname;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckIdRequestDto {

        private String id;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthenticationRequestDto {

        private String id;
        private String password;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignUpRequestDto {

        private String id;
        private String password;
        private String email;
        private Long countryId;
        private Integer emailAlert;
        private String nickname;
        private String birth;
        private String profileImg;
        private UserRole role;

        public User toUser(String encodedPassword, Country country) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            return User.builder()
                .id(this.id)
                .password(encodedPassword)
                .email(this.email)
                .country(country)
                .emailAlert(this.emailAlert)
                .nickname(this.nickname)
                .birth(LocalDate.parse(this.birth, formatter))
                .profileImg(this.profileImg)
                .role(this.role)
                .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateUserRequestDto {

        private String id;
        private String password;
        private String email;
        private Integer emailAlert;
        private Long countryId;
        private String nickname;
        private String profileImg;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FindIdRequestDto {

        private String email;
        private String birth;

        public LocalDate getBirth() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            return LocalDate.parse(birth, formatter);
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FindPasswordRequestDto {

        private String id;
        private String email;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReissueTokensRequestDto {

        private String id;
    }
}
