package com.pi.stepup.domain.dance.dao;

import com.pi.stepup.domain.dance.domain.AttendHistory;
import com.pi.stepup.domain.dance.domain.DanceMusic;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.dance.domain.Reservation;

import java.util.List;
import java.util.Optional;

public interface DanceRepository {

    //랜덤 플레이 댄스 개최
    RandomDance insert(RandomDance randomDance);

    //개최 랜덤 플레이 댄스 하나 조회
    Optional<RandomDance> findOne(Long randomDanceId);

    //개최 랜덤 플레이 댄스 삭제
    void delete(Long randomDanceId);

    //랜덤 플레이 댄스 노래 목록 조회
    List<DanceMusic> findAllDanceMusic(Long randomDanceId);

    //개최 랜덤 플레이 댄스 전체 목록 조회
    List<RandomDance> findAllMyOpenDance(String id);

    //랜덤 플레이 댄스 전체 목록 조회
    List<RandomDance> findAllDance(String keyword);

    //랜덤 플레이 댄스 예약
    Reservation insertReservation(Reservation reservation);

    //예약 랜덤 플레이 댄스 하나 조회
    Optional<Reservation> findReservationByRandomDanceIdAndUserId(Long randomDanceId, Long userId);

    //랜덤 플레이 댄스 예약 취소
    void deleteReservation(Long reservationId);

    //예약 전체 목록 조회
    List<Reservation> findAllMyReservation(Long userId);

    //랜덤 플레이 댄스 참여
    AttendHistory insertAttend(AttendHistory attendHistory);

    //참여 랜덤 플레이 댄스 하나 조회
    Optional<AttendHistory> findAttendByRandomDanceIdAndUserId(Long randomDanceId, Long userId);

    //참여 전체 목록 조회
    List<AttendHistory> findAllMyAttend(Long userId);

}
