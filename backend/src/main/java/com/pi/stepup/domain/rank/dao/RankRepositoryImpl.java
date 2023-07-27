package com.pi.stepup.domain.rank.dao;


import com.pi.stepup.domain.rank.constant.RankName;
import com.pi.stepup.domain.rank.domain.Rank;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RankRepositoryImpl implements RankRepository {

    private final EntityManager em;

    @Override
    public Optional<Rank> findOneByPoint(Integer point) {
        Optional<Rank> rank;
        // TODO : 포인트 범위 안에 있는거 꺼내오기
        try {
            rank = Optional.ofNullable(em.createQuery(
                    "SELECT r FROM Rank r "
                        + "WHERE r.endPoint >= :point AND r.startPoint <= :point", Rank.class
                ).setParameter("point", point)
                .getSingleResult());
        } catch (NoResultException e) {
            rank = Optional.empty();
        }
        return rank;
    }

    @Override
    public Optional<Rank> getRankByName(RankName rankName) {
        Optional<Rank> rank;
        try {
            rank = Optional.ofNullable(em.createQuery(
                "SELECT r FROM Rank r "
                    + "where r.name = :rankName", Rank.class
            ).setParameter("rankName", rankName).getSingleResult());
        } catch (NoResultException e) {
            rank = Optional.empty();
        }
        return rank;
    }
}
