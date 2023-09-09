package com.pi.stepup.domain.music.dao;

import com.pi.stepup.domain.music.domain.Heart;
import com.pi.stepup.domain.music.domain.MusicApply;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
@Slf4j
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
            sql += "WHERE ma.title LIKE '%" + keyword + "%' OR " +
                "ma.artist LIKE '%" + keyword + "%'";
        }

        sql += " ORDER BY ma.musicApplyId DESC";

        return em.createQuery(sql, MusicApply.class).getResultList();
    }

    @Override
    public List<MusicApply> findAllByUserId(Long userId) {
        return em.createQuery("SELECT ma FROM MusicApply ma "
                + "WHERE ma.writer.userId = :userId", MusicApply.class)
            .setParameter("userId", userId)
            .getResultList();
    }

    @Override
    public List<MusicApply> findById(String id) {
        return em.createQuery(
                "SELECT ma FROM MusicApply ma "
                    + "LEFT JOIN FETCH ma.hearts h " +
                    "WHERE ma.writer.id = :id "
                    + "ORDER BY ma.musicApplyId DESC"
                , MusicApply.class
            )
            .setParameter("id", id)
            .getResultList();
    }

    @Override
    public Optional<MusicApply> findOne(Long musicApplyId) {
        Optional<MusicApply> musicApply;

        try {
            musicApply = Optional.ofNullable(em.find(MusicApply.class, musicApplyId));
        } catch (NoResultException e) {
            musicApply = Optional.empty();
        }
        return musicApply;
    }

    @Override
    public Optional<Heart> findHeart(String id, Long musicApplyId) {
        Optional<Heart> heart;

        try {
            heart = Optional.ofNullable(em.createQuery(
                        "SELECT h FROM Heart h " +
                            "WHERE h.user.id = :id " +
                            "AND h.musicApply.musicApplyId = :musicApplyId", Heart.class
                    )
                    .setParameter("id", id)
                    .setParameter("musicApplyId", musicApplyId)
                    .getSingleResult()

            );
        } catch (NoResultException e) {
            heart = Optional.empty();
        }
        return heart;
    }

    @Override
    public List<Heart> findAllHeart() {
        return em.createQuery(
            "SELECT h FROM Heart h "
                + "JOIN FETCH h.user "
                + "JOIN FETCH h.musicApply", Heart.class
        ).getResultList();
    }

    @Override
    public List<Heart> findHeartByMusicApplyId(Long musicApplyId) {
        return em.createQuery(
                "SELECT h FROM Heart h "
                    + "WHERE h.musicApply.musicApplyId = :musicApplyId "
                , Heart.class
            )
            .setParameter("musicApplyId", musicApplyId)
            .getResultList();
    }

    @Override
    public List<Heart> findHeartById(String id) {
        return em.createQuery(
                "SELECT h FROM Heart h "
                    + "WHERE h.user.id = :id", Heart.class
            )
            .setParameter("id", id)
            .getResultList();
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

    @Override
    public void deleteAllHeartsByUserId(Long userId) {
        em.createQuery("DELETE FROM Heart h WHERE h.user.userId = :userId")
            .setParameter("userId", userId)
            .executeUpdate();
    }

    @Override
    public void deleteHeartByIdAndMusicApplyId(String id, Long musicApplyId) {
        try {
            Heart heart = em.createQuery(
                    "SELECT h FROM Heart h "
                        + "WHERE h.user.id = :id AND h.musicApply.musicApplyId = :musicApplyId",
                    Heart.class
                ).setParameter("id", id)
                .setParameter("musicApplyId", musicApplyId)
                .getSingleResult();

            em.remove(heart);
        } catch (NoResultException ex) {
            // 조회 결과가 없는 경우
        }
    }

    @Override
    public void insertHearts(List<Heart> hearts) {
        log.info("[INFO] insert heart repository");
        log.debug("[DEBUG] 삽입되는 좋아요 개수 : {}", hearts.size());
        for (Heart h : hearts) {
            em.persist(h);
        }
    }

    @Override
    public void deleteHearts(List<Heart> hearts) {
        for (Heart h : hearts) {
            em.remove(h);
        }
    }

    @Override
    public void deleteHeartById(String id) {
        List<Heart> hearts = findHeartById(id);
        for (Heart h : hearts) {
            em.remove(h);
        }
    }
}
