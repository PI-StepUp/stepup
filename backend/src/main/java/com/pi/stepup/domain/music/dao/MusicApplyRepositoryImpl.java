package com.pi.stepup.domain.music.dao;

import com.pi.stepup.domain.music.domain.MusicApply;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class MusicApplyRepositoryImpl implements MusicApplyRepository {
    private final EntityManager em;


    @Override
    public MusicApply insert(MusicApply musicApply) {
        em.persist(musicApply);
        return musicApply;
    }
}
