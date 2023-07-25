package com.pi.stepup.domain.music.dao;

import com.pi.stepup.domain.music.domain.MusicApply;

import java.util.List;
import java.util.Optional;

public interface MusicApplyRepository {
    MusicApply insert(MusicApply musicApply);

    Optional<MusicApply> findOne(Long musicApplyId);

    List<MusicApply> findAll(String keyword);

    List<MusicApply> findById(String id);

    void delete(Long musicApplyId);

}
