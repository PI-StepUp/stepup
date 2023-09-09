package com.pi.stepup.domain.music.service;

import java.util.Set;

public interface MusicApplyRedisService {

    void saveHeart(String userId, Long musicApplyId);

    Integer getHeartStatus(String userId, Long musicApplyId);

    Set<Object> getHearts(Long musicApplyId);

    void deleteHeart(String userId, Long musicApplyId);

    boolean checkRedisEmpty();

    void getHeartsFromDB();

    Integer getHeartCnt(Long musicApplyId);
}
