package com.pi.stepup.domain.dance.dao;

import com.pi.stepup.domain.dance.domain.DanceMusic;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.dance.domain.Reservation;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DanceRepositoryImpl implements DanceRepository {

    private final EntityManager em;

    @Override
    public RandomDance insert(RandomDance randomDance) {
        em.persist(randomDance);
        return randomDance;
    }

    @Override
    public Optional<RandomDance> findOne(Long randomDanceId) {
        Optional<RandomDance> randomDance = null;
        try {
            randomDance = Optional.ofNullable(em.find(RandomDance.class, randomDanceId));
        } catch (NoResultException e) {
            randomDance = Optional.empty();
        } finally {
            return randomDance;
        }
    }

    //수정하기
    @Override
    public RandomDance update(RandomDance randomDance) {
        RandomDance findDance = em.find(RandomDance.class, randomDance.getRandomDanceId());
        return em.merge(findDance);
    }

    @Override
    public void delete(Long randomDanceId) {
        RandomDance randomDance = em.find(RandomDance.class, randomDanceId);
        em.remove(randomDance);
    }

    @Override
    public List<DanceMusic> findAllDanceMusic(Long randomDanceId) {
        return em.createQuery("SELECT d FROM DanceMusic d "
                + "WHERE d.randomDance.randomDanceId = :randomDanceId", DanceMusic.class)
            .setParameter("randomDanceId", randomDanceId)
            .getResultList();
    }

    @Override
    public List<RandomDance> findAllDance() {
        return em.createQuery("SELECT r FROM RandomDance r", RandomDance.class)
            .getResultList();
    }

    @Override
    public List<RandomDance> findAllMyOpenDance(String id) {
        return em.createQuery("SELECT r FROM RandomDance r "
                + "WHERE r.host.id = :id", RandomDance.class)
//                        + "JOIN r.host u "
//                        + "WHERE u.id = :id", RandomDance.class)
            .setParameter("id", id)
            .getResultList();
    }

    @Override
    public Reservation insertReservation(Reservation reservation) {
        em.persist(reservation);
        return reservation;
    }

    @Override
    public Optional<Reservation> findReservation(Long randomDanceId, Long userId) {
        Optional<Reservation> reservation = null;
        try {
            reservation = Optional.ofNullable(em.createQuery("SELECT r FROM Reservation r "
                    + "WHERE r.randomDance.randomDanceId = :randomDanceId "
                    + "AND r.user.userId = :userId", Reservation.class)
                .setParameter("randomDanceId", randomDanceId)
                .setParameter("userId", userId)
                .getSingleResult());
        } catch (NoResultException e) {
            reservation = Optional.empty();
        } finally {
            return reservation;
        }

    }

    @Override
    public void deleteReserevation(Long reservationId) {
        Reservation reservation = em.find(Reservation.class, reservationId);
        em.remove(reservation);
    }

}
