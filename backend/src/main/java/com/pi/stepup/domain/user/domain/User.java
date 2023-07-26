package com.pi.stepup.domain.user.domain;

import com.pi.stepup.domain.user.constant.UserRole;
import com.pi.stepup.domain.user.dto.UserRequestDto.UpdateUserRequestDto;
import com.pi.stepup.global.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "USERS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String id;

    private String password;

    private String email;

    private Integer emailAlert;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COUNTRY_ID")
    private Country country;

    private String nickname;

    private LocalDate birth;

    private String profileImg;

    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    private Integer point;

    private String refreshToken;

    // TODO: Rank 엔티티 연관관계 설정

    @Builder
    public User(Long userId, String id, String password, String email, Integer emailAlert,
                Country country, String nickname, LocalDate birth, String profileImg,
                UserRole role, Integer point, String refreshToken) {
        this.userId = userId;
        this.id = id;
        this.password = password;
        this.email = email;
        this.emailAlert = emailAlert;
        this.country = country;
        this.nickname = nickname;
        this.birth = birth;
        this.profileImg = profileImg;
        this.role = role;
        this.point = point;
        this.refreshToken = refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateUserBasicInfo(UpdateUserRequestDto updateUserRequestDto, Country country) {
        this.email = updateUserRequestDto.getEmail();
        this.emailAlert = updateUserRequestDto.getEmailAlert();
        this.country = country;
        this.nickname = updateUserRequestDto.getNickname();
        this.profileImg = updateUserRequestDto.getProfileImg();
    }

    public void updatePassword(String encodedUpdatePassword) {
        this.password = encodedUpdatePassword;
    }

    // TODO : 포인트, 랭크 수정 메서드 추가
    public void updatePoint(Integer point) {
        this.point += point;
    }
}