package com.pi.stepup.domain.music.domain;

import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@RedisHash(value = "heart")
public class HeartForRedis {
    @Id
    private String id;

    private Long musicApplyId;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long ttl;
}
