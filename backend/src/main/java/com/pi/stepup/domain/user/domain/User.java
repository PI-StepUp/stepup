package com.pi.stepup.domain.user.domain;

import com.pi.stepup.domain.user.constant.UserRole;
import com.pi.stepup.global.entity.BaseEntity;
import com.pi.stepup.global.entity.Country;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USERS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    private LocalDateTime birth;

    private String profileImg;

    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    private Integer point;

    // TODO: Rank 엔티티 연관관계 설정
}