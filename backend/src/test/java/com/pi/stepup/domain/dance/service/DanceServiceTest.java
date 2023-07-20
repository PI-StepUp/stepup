package com.pi.stepup.domain.dance.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.pi.stepup.domain.dance.constant.DanceType;
import com.pi.stepup.domain.dance.dao.DanceRepository;
import com.pi.stepup.domain.dance.domain.Dance;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class DanceServiceTest {

    @Autowired
    private DanceRepository danceRepository;

    @Test
    @DisplayName("랜플댄 개최 케이스 테스트")
    @Transactional
    public Dance insert(Dance Dance) {
        //개최할 랜플댄
        Dance insertDance = Dance.builder()
            .title("제1회 랜덤 플레이 댄스")
            .content("같이 놀아요")
            //유저의 국가 코드 전달
            .startAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
            .endAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
            .danceType(DanceType.BASIC)
            .maxUser(30)
            .thumbnail("url")
            .build();

        Dance saveDance = danceRepository.save(insertDance);

        assertThat(saveDance.getId()).isNotNull();
        assertThat(insertDance).isEqualTo(saveDance);

        return insertDance;
    }

}
