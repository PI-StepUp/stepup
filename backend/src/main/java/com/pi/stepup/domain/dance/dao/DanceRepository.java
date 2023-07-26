package com.pi.stepup.domain.dance.dao;

import com.pi.stepup.domain.dance.domain.DanceMusic;
import com.pi.stepup.domain.dance.domain.RandomDance;

import com.pi.stepup.domain.dance.domain.Reservation;
import java.util.List;
import java.util.Optional;
import javax.swing.text.html.Option;

public interface DanceRepository {

    //랜덤 플레이 댄스 개최
    RandomDance insert(RandomDance randomDance);

    //개최 랜덤 플레이 댄스 하나 조회
    Optional<RandomDance> findOne(Long randomDanceId);

    //개최 랜덤 플레이 댄스 수정
    RandomDance update(RandomDance randomDance);

    //개최 랜덤 플레이 댄스 삭제
    void delete(Long randomDanceId);

    //랜덤 플레이 댄스 노래 목록 조회
    List<DanceMusic> findAllDanceMusic(Long randomDanceId);

    //랜덤 플레이 댄스 전체 목록 조회
    List<RandomDance> findAllDance();

    //개최 랜덤 플레이 댄스 전체 목록 조회
    List<RandomDance> findAllMyOpenDance(String id);

    Reservation insertReservation(Reservation reservation);

    Optional<Reservation> findReservation(Long randomDanceId, Long userId);

    void deleteReserevation(Long reservationId);
}
