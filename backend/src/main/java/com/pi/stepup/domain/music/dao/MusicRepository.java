package com.pi.stepup.domain.music.dao;

import com.pi.stepup.domain.music.domain.Music;

public interface MusicRepository {

    Music insert(Music music);
    Music findOne(Long musicId);
}
