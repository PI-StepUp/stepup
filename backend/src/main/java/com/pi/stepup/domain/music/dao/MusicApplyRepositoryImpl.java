package com.pi.stepup.domain.music.dao;

import com.pi.stepup.domain.music.domain.MusicApply;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

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
    public List<MusicApply> findAll(String keyword) {
        String sql = "SELECT ma FROM MusicApply ma";

        if (StringUtils.hasText(keyword) && !keyword.equals("")) {
            sql += "WHERE m.title LIKE concat('%', " + keyword + ", '%') OR " +
                    "m.artist LIKE concat('%', " + keyword + ", '%')";
        }

        return em.createQuery(sql, MusicApply.class).getResultList();
    }
}
