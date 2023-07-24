package com.pi.stepup.domain.music.dao;

import com.pi.stepup.domain.music.domain.MusicApply;

import java.util.List;
import java.util.Optional;

public interface MusicApplyRepository {
    MusicApply insert(MusicApply musicApply);

    List<MusicApply> findAll(String keyword);

    Optional<MusicApply> findOne(Long musicApplyId);

    void delete(Long musicApplyId);
}
