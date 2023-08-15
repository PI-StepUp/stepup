package com.pi.stepup.domain.music.service;

import com.pi.stepup.domain.music.domain.Heart;
import java.util.List;

public interface MusicApplyRedisService {
    void saveHeart(String userId, Long musicApplyId);
    Integer getHeartStatus(String userId, Long musicApplyId);
    List<Heart> getHearts(String userId);
    void deleteHeart(String userId, Long musicApplyId);
}
