package com.pi.stepup.domain.user.dao;

import com.pi.stepup.domain.user.domain.Country;
import com.pi.stepup.domain.user.domain.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<Country> findAllCountries();

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    Optional<User> findById(String id);

}
