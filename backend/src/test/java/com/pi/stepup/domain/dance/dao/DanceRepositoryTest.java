package com.pi.stepup.domain.dance.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.pi.stepup.domain.dance.constant.DanceType;
import com.pi.stepup.domain.dance.domain.RandomDance;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class DanceRepositoryTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private DanceRepository danceRepository;

    private final String title = "랜덤 플레이 댄스";
    private final String content = "함께 합시다";
    private final String userCountry = "Asia/Seoul";
    private final LocalDateTime time = LocalDateTime.now(ZoneId.of(userCountry));
    private final DanceType type = DanceType.BASIC;
    private final int max = 30;
    private final String url = "url";

    @Test
    @DisplayName("랜덤 플레이 댄스 개최 테스트")
    public void insertDanceTest() {
        RandomDance insertDance = RandomDance.builder()
            .title(title)
            .content(content)
            .startAt(time)
            .endAt(time)
            .danceType(type)
            .maxUser(max)
            .thumbnail(url)
            .build();

        RandomDance saveDance = danceRepository.insert(insertDance);

        assertThat(saveDance.getRandomDanceId()).isNotNull();
        assertThat(saveDance.getTitle()).isEqualTo(title);
        assertThat(saveDance.getContent()).isEqualTo(content);

        System.out.println(">>> insertDanceTest 통과");
        System.out.println(saveDance.getRandomDanceId());
    }

    @Test
    @DisplayName("개최 랜덤 플레이 댄스 하나 조회 테스트")
    public void findDanceTest() {
        RandomDance insertDance = RandomDance.builder()
            .title(title)
            .content(content)
            .startAt(time)
            .endAt(time)
            .danceType(type)
            .maxUser(max)
            .thumbnail(url)
            .build();

        em.persist(insertDance);
        RandomDance findDance1 = em.find(RandomDance.class, insertDance.getRandomDanceId());
        RandomDance findDance2 = danceRepository.findOne(insertDance.getRandomDanceId());

        assertThat(findDance2).isNotNull();
        assertThat(findDance2.getTitle()).isEqualTo(findDance1.getTitle());
        assertThat(findDance2.getContent()).isEqualTo(findDance1.getContent());

        System.out.println(">>> findRandomDanceTest 통과");
        System.out.println(findDance2.getRandomDanceId());
    }

    @Test
    @DisplayName("개최 랜덤 플레이 댄스 수정 테스트")
    public void updateDanceTest() {
        RandomDance insertDance = RandomDance.builder()
            .title(title)
            .content(content)
            .startAt(time)
            .endAt(time)
            .danceType(type)
            .maxUser(max)
            .thumbnail(url)
            .build();
        em.persist(insertDance);

        RandomDance findDance = em.find(RandomDance.class, insertDance.getRandomDanceId());

        String updatedTitle = "제목 업데이트";
        String updatedContent = "내용 업데이트";

        RandomDance updatedDance = RandomDance.builder()
            .id(findDance.getRandomDanceId())
            .title(updatedTitle)
            .content(updatedContent)
            .startAt(findDance.getStartAt())
            .endAt(findDance.getEndAt())
            .danceType(findDance.getDanceType())
            .maxUser(findDance.getMaxUser())
            .thumbnail(findDance.getThumbnail())
            .build();

        danceRepository.update(updatedDance);

        assertThat(findDance.getTitle()).isEqualTo(updatedTitle);
        assertThat(findDance.getContent()).isEqualTo(updatedContent);

        System.out.println(">>> updateDanceTest 통과");
        System.out.println(findDance.getRandomDanceId());
        System.out.println(findDance.getTitle());
    }

    @Test
    @DisplayName("개최 랜덤 플레이 댄스 삭제 테스트")
    @Transactional
    public void deleteDanceTest() {
        RandomDance insertDance = RandomDance.builder()
            .title(title)
            .content(content)
            .startAt(time)
            .endAt(time)
            .danceType(type)
            .maxUser(max)
            .thumbnail(url)
            .build();
        em.persist(insertDance);

        Long randomDanceId = insertDance.getRandomDanceId();
        RandomDance findDance1 = em.find(RandomDance.class, randomDanceId);
        System.out.println(findDance1.getTitle() + " " + findDance1.getContent());
        assertThat(findDance1).isNotNull();

        danceRepository.delete(randomDanceId);

        RandomDance findDance2 = em.find(RandomDance.class, randomDanceId);
        assertThat(findDance2).isNull();

        System.out.println(">>> deleteDanceTest 통과");
        System.out.println(findDance2);
    }

    @Test
    @DisplayName("개최 랜덤 플레이 댄스 전체 목록 조회 테스트")
    public void findAllHeldDanceTest() {
        for (int i = 0; i < 10; i++) {
            RandomDance insertDance = RandomDance.builder()
                .title(title + i)
                .content(content)
                .startAt(time)
                .endAt(time)
                .danceType(type)
                .maxUser(max)
                .thumbnail(url)
                .build();
            em.persist(insertDance);
        }

        List<RandomDance> randomDanceList = danceRepository.findAllHeldDance();

        assertThat(randomDanceList.size()).isEqualTo(10);
        assertThat(randomDanceList).isNotNull();

        System.out.println(">>> findAllRandomDanceTest 통과");
        System.out.println(randomDanceList.size());
//        danceRepository.findAllRandomDance().forEach(System.out::println);
    }
}
