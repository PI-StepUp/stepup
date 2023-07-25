package com.pi.stepup.domain.dance.dao;

import com.pi.stepup.domain.dance.domain.RandomDance;
import java.util.List;
import java.util.Optional;

public interface DanceRepository {

    //랜덤 플레이 댄스 개최
    RandomDance insert(RandomDance randomDance);

    //개최 랜덤 플레이 댄스 하나 조회
    Optional<RandomDance> findOne(Long randomDanceId);

    //개최 랜덤 플레이 댄스 수정
    RandomDance update(RandomDance randomDance);

    //개최 랜덤 플레이 댄스 삭제
    void delete(Long randomDanceId);

    //개최 랜덤 플레이 댄스 전체 목록 조회
    List<RandomDance> findAllHeldDance();

}
