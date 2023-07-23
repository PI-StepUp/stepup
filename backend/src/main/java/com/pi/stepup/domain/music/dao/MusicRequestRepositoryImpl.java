package com.pi.stepup.domain.music.dao;

import com.pi.stepup.domain.music.domain.MusicRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class MusicRequestRepositoryImpl implements MusicRequestRepository {
    private final EntityManager em;


    @Override
    public MusicRequest insert(MusicRequest musicRequest) {
        em.persist(musicRequest);
        return musicRequest;
    }
}
