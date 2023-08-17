package com.pi.stepup.domain.rank.dao;

import com.pi.stepup.domain.rank.domain.PointHistory;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PointHistoryRepositoryImpl implements PointHistoryRepository {

    private final EntityManager em;

    @Override
    public List<PointHistory> findAll(String id) {
        return em.createQuery(
                "SELECT ph FROM PointHistory ph "
                    + "WHERE ph.user.id = :id", PointHistory.class
            )
            .setParameter("id", id)
            .getResultList();
    }

    @Override
    public PointHistory insert(PointHistory pointHistory) {
        em.persist(pointHistory);
        return pointHistory;
    }

    @Override
    public void deleteAllByUserId(Long userId) {
        em.createQuery("DELETE FROM PointHistory ph WHERE ph.user.userId = :userId")
            .setParameter("userId", userId)
            .executeUpdate();
    }
}
