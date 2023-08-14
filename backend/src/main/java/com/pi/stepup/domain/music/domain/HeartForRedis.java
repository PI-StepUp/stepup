package com.pi.stepup.domain.music.domain;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash(value = "heart")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class HeartForRedis {
    @Id
    private String id;

    private HashSet<Long> musicApplyIds;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long ttl;

    public void addMusicApply(Long musicApplyId) {
        this.musicApplyIds.add(musicApplyId);
    }
}
