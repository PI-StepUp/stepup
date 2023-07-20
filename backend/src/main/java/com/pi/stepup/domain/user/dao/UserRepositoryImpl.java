package com.pi.stepup.domain.user.dao;

import com.pi.stepup.domain.user.domain.Country;
import com.pi.stepup.domain.user.domain.User;
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

    @Override
    public User findByEmail(String email) {
        return em.createQuery(
                "SELECT u FROM User u "
                    + "WHERE u.email = :email", User.class)
            .setParameter("email", email)
            .getSingleResult();
    }
}
