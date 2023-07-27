package com.pi.stepup.domain.rank.dao;

import com.pi.stepup.domain.rank.constant.RankName;
import com.pi.stepup.domain.rank.domain.Rank;
import java.util.Optional;

public interface RankRepository {

    Optional<Rank> findOneByPoint(Integer point);

    Optional<Rank> getRankByName(RankName rankName);
}
