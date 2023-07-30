package com.pi.stepup.domain.dance.dao;

import com.pi.stepup.domain.dance.domain.AttendHistory;
import com.pi.stepup.domain.dance.domain.DanceMusic;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.dance.domain.Reservation;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

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
    public List<RandomDance> findAllMyOpenDance(String id) {
        return em.createQuery("SELECT r FROM RandomDance r "
                + "WHERE r.host.id = :id", RandomDance.class)
//                        + "JOIN r.host u "
//                        + "WHERE u.id = :id", RandomDance.class)
            .setParameter("id", id)
            .getResultList();
    }

    @Override
    public List<RandomDance> findAllDance(String keyword) {
        String sql = "SELECT r FROM RandomDance r ";

        if (StringUtils.hasText(keyword) && !keyword.equals("")) {
            sql += "WHERE r.title LIKE concat('%', " + keyword + ", '%') OR " +
                "r.content LIKE concat('%', " + keyword + ", '%')";
        }

        return em.createQuery(sql, RandomDance.class).getResultList();
    }

    @Override
    public Reservation insertReservation(Reservation reservation) {
        em.persist(reservation);
        return reservation;
    }

    @Override
    public Optional<Reservation> findReservationByRandomDanceIdAndUserId(Long randomDanceId, Long userId) {
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
    public void deleteReservation(Long reservationId) {
        Reservation reservation = em.find(Reservation.class, reservationId);
        em.remove(reservation);
    }

    @Override
    public List<Reservation> findAllMyReservation(Long userId) {
        return em.createQuery("SELECT r FROM Reservation r "
                + "WHERE r.user.userId = :userId", Reservation.class)
            .setParameter("userId", userId)
            .getResultList();
    }

    @Override
    public AttendHistory insertAttend(AttendHistory attendHistory) {
        em.persist(attendHistory);
        return attendHistory;
    }

    @Override
    public Optional<AttendHistory> findAttendByRandomDanceIdAndUserId(Long randomDanceId, Long userId) {
        Optional<AttendHistory> attend = null;
        try {
            attend = Optional.ofNullable(em.createQuery("SELECT a FROM AttendHistory a "
                            + "WHERE a.randomDance.randomDanceId = :randomDanceId "
                            + "AND a.user.userId = :userId", AttendHistory.class)
                    .setParameter("randomDanceId", randomDanceId)
                    .setParameter("userId", userId)
                    .getSingleResult());
        } catch (NoResultException e) {
            attend = Optional.empty();
        } finally {
            return attend;
        }
    }

    @Override
    public List<AttendHistory> findAllMyAttend(Long userId) {
        return em.createQuery("SELECT a FROM AttendHistory a "
                + "WHERE a.user.userId = :userId", AttendHistory.class)
            .setParameter("userId", userId)
            .getResultList();
    }
}
