package com.pi.stepup.domain.music.dao;

import com.pi.stepup.domain.music.domain.Heart;
import com.pi.stepup.domain.music.domain.MusicApply;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class MusicApplyRepositoryImpl implements MusicApplyRepository {

    private final EntityManager em;

    @Override
    public MusicApply insert(MusicApply musicApply) {
        em.persist(musicApply);
        return musicApply;
    }

    @Override
    public Heart insert(Heart heart) {
        em.persist(heart);
        return heart;
    }

    @Override
    public List<MusicApply> findAll(String keyword) {
        String sql = "SELECT ma FROM MusicApply ma ";

        if (StringUtils.hasText(keyword) && !keyword.equals("")) {
            sql += "WHERE ma.title LIKE concat('%', " + keyword + ", '%') OR " +
                "ma.artist LIKE concat('%', " + keyword + ", '%')";
        }

        return em.createQuery(sql, MusicApply.class).getResultList();
    }

    @Override
    public List<MusicApply> findById(String id) {
        return em.createQuery(
                "SELECT ma FROM MusicApply ma " +
                    "WHERE ma.writer.id = :id", MusicApply.class
            )
            .setParameter("id", id)
            .getResultList();
    }

    @Override
    public Optional<MusicApply> findOne(Long musicApplyId) {
        Optional<MusicApply> musicApply = null;

        try {
            musicApply = Optional.ofNullable(em.find(MusicApply.class, musicApplyId));
        } catch (NoResultException e) {
            musicApply = Optional.empty();
        }
        return musicApply;
    }

    @Override
    public Heart findHeart(String id, Long musicApplyId) {
        return em.createQuery(
                "SELECT h FROM Heart h " +
                    "WHERE h.user.id = :id " +
                    "AND h.musicApply.musicApplyId = :musicApplyId", Heart.class
            )
            .setParameter("id", id)
            .setParameter("musicApplyId", musicApplyId)
            .getSingleResult();
    }

    @Override
    public void delete(Long musicApplyId) {
        MusicApply musicApply = em.find(MusicApply.class, musicApplyId);
        em.remove(musicApply);
    }

    @Override
    public void deleteHeart(Long heartId) {
        Heart heart = em.find(Heart.class, heartId);
        em.remove(heart);
    }
}
