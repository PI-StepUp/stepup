package com.pi.stepup.domain.dance.dao;

import com.pi.stepup.domain.dance.domain.AttendHistory;
import com.pi.stepup.domain.dance.domain.DanceMusic;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.dance.domain.Reservation;
import java.util.List;
import java.util.Optional;

public interface DanceRepository {

    RandomDance insert(RandomDance randomDance);

    Optional<RandomDance> findOne(Long randomDanceId);

    void delete(Long randomDanceId);

    void updateAllHostDeletedByUserID(Long userId);

    List<DanceMusic> findAllDanceMusic(Long randomDanceId);

    List<RandomDance> findAllMyOpenDance(String id);

    List<RandomDance> findAllDance(String keyword);

    List<RandomDance> findScheduledDance(String keyword);

    List<RandomDance> findInProgressDance(String keyword);

    Reservation insertReservation(Reservation reservation);

    Optional<Reservation> findReservationByRandomDanceIdAndUserId(Long randomDanceId, Long userId);

    Optional<Reservation> findReservationByReservationIdAndRandomDanceId
        (Long reservationId, Long randomDanceId);

    void deleteReservation(Long randomDanceId, Long userId);

    void deleteAllReservationByUserId(Long userId);

    List<Reservation> findAllMyReservation(Long userId);

    AttendHistory insertAttend(AttendHistory attendHistory);

    void deleteAllAttendByUserId(Long userId);

    Optional<AttendHistory> findAttendByRandomDanceIdAndUserId(Long randomDanceId, Long userId);

    List<AttendHistory> findAllMyAttend(Long userId);

}
