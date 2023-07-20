package com.pi.stepup.domain.user.dao;

import com.pi.stepup.domain.user.domain.Country;
import com.pi.stepup.domain.user.domain.User;
import java.util.List;

public interface UserRepository {

    List<Country> findAllCountries();

    User findByEmail(String email);

}
