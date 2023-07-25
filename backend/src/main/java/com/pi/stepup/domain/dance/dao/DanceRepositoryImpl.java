package com.pi.stepup.domain.dance.dao;

import com.pi.stepup.domain.dance.domain.RandomDance;
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

    //상세 페이지는 없지만 수정할 때 필요?
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
    public List<RandomDance> findAllHeldDance() {
        return em.createQuery("SELECT r FROM RandomDance r", RandomDance.class)
            .getResultList();
    }

}
