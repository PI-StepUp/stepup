package com.pi.stepup.domain.rank.dao;

import com.pi.stepup.domain.rank.domain.PointPolicy;
import java.util.Optional;

public interface PointPolicyRepository {

    Optional<PointPolicy> findOne(Long pointPolicyId);
}
