package com.pi.stepup.domain.music.dao;

import com.pi.stepup.domain.music.domain.Music;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MusicRepositoryImpl implements MusicRepository {
    private final EntityManager em;

    @Override
    public Music insert(Music music) {
        em.persist(music);
        return music;
    }

    @Override
    public Music findOne(Long musicId) {
        return em.find(Music.class, musicId);
    }
}
