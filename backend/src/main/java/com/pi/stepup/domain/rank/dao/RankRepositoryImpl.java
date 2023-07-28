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

        // 방법 1 : 사용자 포인트 기준으로 랭크 이름 바로 뽑기
        String sql = "SELECT ID, POINT,\n" +
            "CASE WHEN POINT BETWEEN 0 AND 99 THEN 'BRONZE'\n" +
            "WHEN POINT BETWEEN 100 AND 299 THEN 'SILVER'\n" +
            "WHEN POINT BETWEEN 300 AND 999 THEN 'GOLD'\n" +
            "ELSE 'PLATINUM' \n" +
            "END AS RANK_NAME\n" +
            "FROM USERS";

        // 하고싶은 방법 : Rank 테이블 조회하면서 유저 포인트가 StartPoint ~ EndPoint 안에 들어가면 해당하는 Rank PK 뽑아오기

        try {
            rank = Optional.ofNullable(em.createQuery(
                    "SELECT r FROM Rank r "
                        + "WHERE :point BETWEEN r.startPoint AND r.endPoint", Rank.class
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
