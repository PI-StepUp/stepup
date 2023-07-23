package com.pi.stepup.domain.dance.dao;

import com.pi.stepup.domain.dance.domain.RandomDance;
import java.util.List;
import javax.persistence.EntityManager;
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

    //상세 페이지는 없지만 수정할 때 필요?
    @Override
    public RandomDance findOne(Long randomDanceId) {
        return em.find(RandomDance.class, randomDanceId);
    }

    @Override
    public RandomDance update(RandomDance randomDance) {
        RandomDance updateDance = em.merge(randomDance);
        return updateDance;
    }

    @Override
    public void delete(Long randomDanceId) {
        RandomDance randomDance = em.find(RandomDance.class, randomDanceId);
        em.remove(randomDance);
    }

    @Override
    public List<RandomDance> findAllHeldDance() {
        return em.createQuery("SELECT r FROM RandomDance r", RandomDance.class)
            .getResultList();
    }

}
