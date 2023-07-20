package com.pi.stepup.domain.dance.dao;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

import com.pi.stepup.domain.dance.constant.DanceType;
import com.pi.stepup.domain.dance.domain.Dance;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class DanceRepositoryTest {

    @Autowired
    private DanceRepository danceRepository;

    @Test
    @DisplayName("랜플댄 개최 테스트")
    @Transactional
    public void insertDanceTest() {
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
    }

    @Test
    @DisplayName("랜플댄 하나 조회 테스트")
    @Transactional
    public void findDanceTest() {
        //조회할 번호
        Long id = 1L;
        Optional<Dance> findDance = danceRepository.findById(id);

        assertThat(findDance).isNotNull();
    }

    //테스트는 통과되는데, 쿼리문이 나오지 않는다
    @Test
    @DisplayName("랜플댄 수정 테스트")
    @Transactional
    public void updateDanceTest() {
        //수정할 랜플댄 조회
//        Long id = 1L;
//        Optional<Dance> findDance = danceRepository.findById(id);
//
//        findDance.ifPresent(selectDance -> {
//            selectDance.setTitle("수정");
//            Dance updateDance = danceRepository.save(selectDance);
//            assertThat(updateDance.getTitle()).isEqualTo("수정");
//        });

        //수정할 랜플댄
        Dance updateDance = Dance.builder()
            .id(1L)
            .title("수정")
            .content("테스트")
            .build();

        Dance newDance = danceRepository.save(updateDance);
        assertThat(newDance.getTitle()).isEqualTo("수정");
    }

    //테스트는 통과되는데, 쿼리문이 나오지 않는다
    @Test
    @DisplayName("랜플댄 삭제 테스트")
    @Transactional
    public void deleteDanceTest() {
        try {
            //삭제할 랜플댄 번호
            Long id = 1L;
            danceRepository.deleteById(id);

            assertThat(danceRepository.getById(1L)).isNull();
        } catch (EmptyResultDataAccessException e) {
            System.out.println("EmptyResultDataAccessException 발생");
        }
    }

//    @Test
//    @DisplayName("랜플댄 전체 목록 테스트")
//    public void findAllDanceTest() {
//        List<Dance> danceList = danceRepository.findAll();
//    }
}
