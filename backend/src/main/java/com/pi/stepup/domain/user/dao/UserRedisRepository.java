package com.pi.stepup.domain.user.dao;

import com.pi.stepup.domain.user.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface UserRedisRepository extends CrudRepository<RefreshToken, String> {

}
