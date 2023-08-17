package com.pi.stepup.domain.music.dao;

import com.pi.stepup.domain.music.domain.Heart;
import com.pi.stepup.domain.music.domain.MusicApply;
import java.util.List;
import java.util.Optional;

public interface MusicApplyRepository {

    MusicApply insert(MusicApply musicApply);

    Heart insert(Heart heart);

    Optional<MusicApply> findOne(Long musicApplyId);

    Optional<Heart> findHeart(String id, Long musicApplyId);

    List<MusicApply> findAll(String keyword);

    List<MusicApply> findAll(String keyword, String id);

    List<MusicApply> findAllByUserId(Long userId);

    List<MusicApply> findById(String id);

    void delete(Long musicApplyId);

    void deleteHeart(Long heartId);

    void deleteAllHeartsByUserId(Long userId);
}
