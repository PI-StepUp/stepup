package com.pi.stepup.domain.user.dao;

import com.pi.stepup.domain.user.domain.UserInfo;
import org.springframework.data.repository.CrudRepository;

public interface UserInfoRedisRepository extends CrudRepository<UserInfo, String> {

}
