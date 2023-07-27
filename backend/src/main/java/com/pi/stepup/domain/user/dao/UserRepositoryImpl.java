package com.pi.stepup.domain.user.dao;

import com.pi.stepup.domain.user.domain.Country;
import com.pi.stepup.domain.user.domain.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
    public Country findOneCountry(Long countryId) {
        return em.find(Country.class, countryId);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<User> user = null;

        try {
            user = Optional.ofNullable(em.createQuery(
                    "SELECT u FROM User u "
                        + "WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult());
        } catch (NoResultException e) {
            user = Optional.empty();
        } finally {
            return user;
        }
    }

    @Override
    public Optional<User> findByNickname(String nickname) {
        Optional<User> user = null;

        try {
            user = Optional.ofNullable(
                em.createQuery(
                        "SELECT u FROM User u "
                            + "WHERE u.nickname = :nickname", User.class
                    )
                    .setParameter("nickname", nickname)
                    .getSingleResult()
            );
        } catch (NoResultException e) {
            user = Optional.empty();
        } finally {
            return user;
        }
    }

    @Override
    public Optional<User> findById(String id) {
        Optional<User> user = null;

        try {
            user = Optional.ofNullable(
                em.createQuery(
                        "SELECT u FROM User u "
                            + "WHERE u.id = :id", User.class
                    )
                    .setParameter("id", id)
                    .getSingleResult()
            );
        } catch (NoResultException e) {
            user = Optional.empty();
        } finally {
            return user;
        }
    }

    @Override
    public User insert(User user) {
        em.persist(user);

        return user;
    }

    @Override
    public void delete(User user) {
        em.remove(user);
    }

    @Override
    public Optional<User> findByEmailAndBirth(String email, LocalDate birth) {
        Optional<User> user = null;

        try {
            user = Optional.ofNullable(
                em.createQuery(
                        "SELECT u FROM User u "
                            + "WHERE u.email = :email AND u.birth = :birth", User.class
                    )
                    .setParameter("email", email)
                    .setParameter("birth", birth)
                    .getSingleResult()
            );
        } catch (NoResultException e) {
            user = Optional.empty();
        } finally {
            return user;
        }
    }

    @Override
    public Optional<User> findByIdAndEmail(String id, String email) {
        Optional<User> user = null;

        try {
            user = Optional.ofNullable(
                em.createQuery(
                        "SELECT u FROM User u "
                            + "WHERE u.id = :id AND u.email = :email", User.class
                    )
                    .setParameter("id", id)
                    .setParameter("email", email)
                    .getSingleResult()
            );
        } catch (NoResultException e) {
            user = Optional.empty();
        } finally {
            return user;
        }
    }
}
