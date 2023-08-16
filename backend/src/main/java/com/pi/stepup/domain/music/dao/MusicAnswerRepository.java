package com.pi.stepup.domain.music.dao;

import com.pi.stepup.domain.music.domain.MusicAnswer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MusicAnswerRepository extends MongoRepository<MusicAnswer, String> {

}
