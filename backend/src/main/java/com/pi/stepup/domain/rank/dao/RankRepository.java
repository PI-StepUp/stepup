package com.pi.stepup.domain.rank.dao;

import com.pi.stepup.domain.rank.domain.UserRank;

import java.util.Optional;

public interface RankRepository {

    Optional<UserRank> findByUserId(String id);
}
