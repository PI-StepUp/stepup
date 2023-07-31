package com.pi.stepup.domain.dance.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.pi.stepup.domain.dance.domain.DanceMusic;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.dance.domain.Reservation;
import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.user.domain.User;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
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

    private RandomDance randomDance;
    private Music music;
    private DanceMusic danceMusic;
    private User host;
    private User user;
    private Reservation reservation;
    private Long pk = 1L;
    private final String title = "랜덤 플레이 댄스";
    private final String content = "함께 합시다";
    private final String mTitle = "ISTJ";
    private final String artist = "NCT DREAM";
    private String id = "id";

    @Test
    @BeforeEach
    public void init() {
        randomDance = RandomDance.builder()
            .title(title)
            .content(content)
            .build();

        music = Music.builder()
            .title(mTitle)
            .artist(artist)
            .build();
        em.persist(music);

        danceMusic = DanceMusic.createDanceMusic(music);
        randomDance.addDanceMusicAndSetThis(danceMusic);

        host = User.builder()
            .id(id)
            .build();
        em.persist(host);

        user = User.builder()
            .id(id)
            .build();
        em.persist(user);

        reservation = Reservation.builder()
            .randomDance(randomDance)
            .user(user)
            .build();
    }

    @Test
    @DisplayName("랜덤 플레이 댄스 개최 테스트")
    public void insertDanceTest() {
        RandomDance saveDance = danceRepository.insert(randomDance);

        assertThat(saveDance).isEqualTo(randomDance);
        assertThat(saveDance.getRandomDanceId()).isEqualTo(pk);
        assertThat(saveDance.getTitle()).isEqualTo(title);
        assertThat(saveDance.getContent()).isEqualTo(content);
    }

    @Test
    @DisplayName("개최 랜덤 플레이 댄스 하나 조회 테스트")
    public void findDanceTest() {
        em.persist(randomDance);

        RandomDance findDance1 = em.find(RandomDance.class, randomDance.getRandomDanceId());
        RandomDance findDance2 = danceRepository.findOne(randomDance.getRandomDanceId())
            .orElseThrow();

        assertThat(findDance2).isNotNull();
        assertThat(findDance1).isEqualTo(findDance2);
        assertThat(findDance2.getTitle()).isEqualTo(findDance1.getTitle());
        assertThat(findDance2.getContent()).isEqualTo(findDance1.getContent());
    }

    @Test
    @DisplayName("개최 랜덤 플레이 댄스 삭제 테스트")
    public void deleteDanceTest() {
        em.persist(randomDance);

        danceRepository.delete(randomDance.getRandomDanceId());

        RandomDance findDance = em.find(RandomDance.class, randomDance.getRandomDanceId());

        assertThat(findDance).isNull();
    }

    @Test
    @DisplayName("개최 랜덤 플레이 댄스 노래 목록 조회 테스트")
    public void findAllDanceMusicTest() {
        em.persist(randomDance);

        List<DanceMusic> danceMusicList
            = danceRepository.findAllDanceMusic(randomDance.getRandomDanceId());

        assertThat(danceMusicList).isNotNull();
        assertThat(danceMusicList.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("개최 랜덤 플레이 댄스 목록 조회 테스트")
    public void findAllMyOpenDanceTest() {
        for (int i = 0; i < 10; i++) {
            randomDance = RandomDance.builder()
                .title(title + i)
                .content(content + i)
                .host(host)
                .build();
            em.persist(randomDance);
        }

        List<RandomDance> randomDanceList
            = danceRepository.findAllMyOpenDance(host.getId());

        assertThat(randomDanceList.size()).isEqualTo(10);
    }

    //TODO: 전체 목록 조회 3개

    @Test
    @DisplayName("랜덤 플레이 댄스 예약 테스트")
    public void insertReservationTest() {
        Reservation saveReservation
            = danceRepository.insertReservation(reservation);

        assertThat(saveReservation).isEqualTo(reservation);
        assertThat(saveReservation.getReservationId()).isEqualTo(pk);
        assertThat(saveReservation.getRandomDance()).isEqualTo(randomDance);
    }

    @Test
    @DisplayName("예약한 랜덤 플레이 댄스 하나 조회 테스트")
    public void findReservationTest() {
        em.persist(randomDance);
        em.persist(reservation);

        Reservation findReservation1 = em.find(Reservation.class, reservation.getReservationId());
        Reservation findReservation2
            = danceRepository.findReservationByRandomDanceIdAndUserId
            (reservation.getRandomDance().getRandomDanceId(),
                reservation.getUser().getUserId()).orElseThrow();

        assertThat(findReservation2).isNotNull();
        assertThat(findReservation1).isEqualTo(findReservation2);
    }

    @Test
    @DisplayName("랜덤 플레이 댄스 예약 취소")
    public void deleteReservationTest() {
        em.persist(randomDance);
        em.persist(reservation);

        Reservation findReservation = em.find(Reservation.class, reservation.getReservationId());

        assertThat(findReservation).isNotNull();

        danceRepository.deleteReservation
            (reservation.getRandomDance().getRandomDanceId(),
                reservation.getUser().getUserId());

//        assertThat(findReservation).isNull();
    }
}
