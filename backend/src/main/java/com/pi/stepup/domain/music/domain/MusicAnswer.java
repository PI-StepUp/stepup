package com.pi.stepup.domain.music.domain;

import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;

@Document(collection = "musicAnswers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MusicAnswer {

    @Id
    private String id;

    private String answer;

    @Builder
    public MusicAnswer(String id, String answer) {
        this.id = id;
        this.answer = answer;
    }

    public void updateMusicAnswer(String answer) {
        if (StringUtils.hasText(answer)) {
            this.answer = answer;
        }
    }
}
