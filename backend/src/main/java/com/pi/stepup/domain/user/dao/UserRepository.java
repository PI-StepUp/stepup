package com.pi.stepup.domain.user.dao;

import com.pi.stepup.domain.user.domain.Country;
import java.util.List;

public interface UserRepository {

    List<Country> findAllCountries();

}
