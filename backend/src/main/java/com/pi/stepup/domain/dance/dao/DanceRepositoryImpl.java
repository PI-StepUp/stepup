package com.pi.stepup.domain.dance.dao;


import com.pi.stepup.domain.dance.domain.Dance;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

//@Repository
@RequiredArgsConstructor
public class DanceRepositoryImpl implements DanceRepository {

    private final EntityManager em;

    @Override
    public Dance insert(Dance dance) {
        if(dance.getId()==null) {
            em.persist(dance);
        } else {
            em.merge(dance);
        }
        return dance;
    }

//    @Override
//    public Dance update(Dance dance) {
//        return dance;
//    }

//    @Override
//    public Dance findOne(Long id) {
//        return em.find(Dance.class, id);
//    }
}
