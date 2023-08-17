package com.pi.stepup.domain.rank.dao;

import com.pi.stepup.domain.rank.domain.PointHistory;
import java.util.List;

public interface PointHistoryRepository {

    List<PointHistory> findAll(String id);

    PointHistory insert(PointHistory pointHistory);
}
