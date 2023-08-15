package com.pi.stepup.domain.rank.service;

import com.pi.stepup.domain.rank.constant.RankName;

public interface PointRedisService {
    public void updatePoint(String id, Integer pointToAdd);

    void updateRank(String id, RankName rankName);
}
