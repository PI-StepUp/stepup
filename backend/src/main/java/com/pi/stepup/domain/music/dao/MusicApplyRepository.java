package com.pi.stepup.domain.music.dao;

import com.pi.stepup.domain.music.domain.MusicApply;

import java.util.List;

public interface MusicApplyRepository {
    MusicApply insert(MusicApply musicApply);

    List<MusicApply> findAll(String keyword);
}
