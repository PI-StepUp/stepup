package com.pi.stepup.domain.rank.dao;

import com.pi.stepup.domain.rank.domain.PointHistory;

import java.util.List;

public interface PointHistoryRepository {
    List<PointHistory> findAll();

    PointHistory insert(PointHistory pointHistory);
}
