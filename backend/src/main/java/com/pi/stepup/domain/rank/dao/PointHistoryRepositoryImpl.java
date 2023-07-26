package com.pi.stepup.domain.rank.dao;

import com.pi.stepup.domain.user.domain.User;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PointHistoryRepositoryImpl implements PointHistoryRepository{
    EntityManager em;

    @Override
    public User update(Integer point) {
        // TODO : 유저 정보 가져와서 포인트 바꾸는 함수 호출
        em.find()
        return null;
    }
}
