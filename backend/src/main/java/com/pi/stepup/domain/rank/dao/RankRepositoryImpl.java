package com.pi.stepup.domain.rank.dao;


import com.pi.stepup.domain.rank.domain.UserRank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RankRepositoryImpl implements RankRepository {
    private final EntityManager em;

    @Override
    public Optional<UserRank> findByUserId(String id) {
        Optional<UserRank> userRank;
        try {
            userRank = Optional.ofNullable(em.createQuery(
                                    "SELECT ur FROM UserRank ur " +
                                            "WHERE ur.user.id = :id", UserRank.class
                            )
                            .setParameter("id", id)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            userRank = Optional.empty();
        }
        return userRank;
    }
}
