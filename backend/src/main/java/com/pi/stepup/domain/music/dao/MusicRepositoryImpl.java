package com.pi.stepup.domain.music.dao;

import com.pi.stepup.domain.music.domain.Music;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

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
    public Optional<Music> findOne(Long musicId) {
        Optional<Music> music = null;

        try {
            music = Optional.ofNullable(em.find(Music.class, musicId));
        } catch (NoResultException e) {
            music = Optional.empty();
        } finally {
            return music;
        }
    }

    @Override
    public List<Music> findAll(String keyword) {
        String sql = "SELECT m FROM Music m ";

        if (StringUtils.hasText(keyword) && !keyword.equals("")) {
            sql += "WHERE m.title LIKE '%" + keyword + "%' OR " +
                "m.artist LIKE '%" + keyword + "%'";
        }

        return em.createQuery(sql, Music.class).getResultList();
    }

    @Override
    public void delete(Long musicId) {
        Music music = em.find(Music.class, musicId);
        em.remove(music);
    }
}
