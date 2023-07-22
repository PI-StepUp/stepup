package com.pi.stepup.domain.music.dao;

import com.pi.stepup.domain.music.domain.Music;

import java.util.Optional;

public interface MusicRepository {

    Music insert(Music music);
    Optional<Music> findOne(Long musicId);
}
