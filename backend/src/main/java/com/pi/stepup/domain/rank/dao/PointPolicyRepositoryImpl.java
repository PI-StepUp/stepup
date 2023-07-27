package com.pi.stepup.domain.rank.dao;

import com.pi.stepup.domain.rank.domain.PointPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PointPolicyRepositoryImpl implements PointPolicyRepository {
    private final EntityManager em;

    @Override
    public Optional<PointPolicy> findOne(Long pointPolicyId) {
        Optional<PointPolicy> pointPolicy;
        try {
            pointPolicy = Optional.ofNullable(em.find(PointPolicy.class, pointPolicyId));
        } catch (NoResultException e) {
            pointPolicy = Optional.empty();
        }
        return pointPolicy;
    }
}
