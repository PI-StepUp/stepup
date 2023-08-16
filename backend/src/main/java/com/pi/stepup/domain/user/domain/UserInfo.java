package com.pi.stepup.domain.user.domain;

import com.pi.stepup.domain.user.constant.UserRole;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@RedisHash(value = "user_info")
public class UserInfo {

    @Id
    private String id;

    private String email;
    private Integer emailAlert;
    private Long countryId;
    private String countryCode;
    private String nickname;
    private LocalDate birth;
    private String profileImg;
    private Integer point;
    private String rankName;
    private String rankImg;
    private UserRole role;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long ttl;

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }
}
