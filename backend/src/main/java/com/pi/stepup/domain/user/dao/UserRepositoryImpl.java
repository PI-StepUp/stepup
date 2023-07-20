package com.pi.stepup.domain.user.dao;

import com.pi.stepup.domain.user.domain.Country;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final EntityManager em;

    @Override
    public List<Country> findAllCountries() {
        return em.createQuery("SELECT c FROM Country c", Country.class)
            .getResultList();
    }
}
