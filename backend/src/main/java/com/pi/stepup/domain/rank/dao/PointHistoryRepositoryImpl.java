package com.pi.stepup.domain.rank.dao;

import com.pi.stepup.domain.rank.domain.PointHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PointHistoryRepositoryImpl implements PointHistoryRepository {
    private final EntityManager em;

    @Override
    public List<PointHistory> findAll() {
        return em.createQuery(
                        "SELECT ph FROM PointHistory ph", PointHistory.class
                )
                .getResultList();
    }

    @Override
    public PointHistory insert(PointHistory pointHistory) {
        em.persist(pointHistory);
        return pointHistory;
    }
}
