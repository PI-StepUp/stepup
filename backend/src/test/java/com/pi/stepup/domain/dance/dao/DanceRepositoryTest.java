package com.pi.stepup.domain.dance.dao;

import com.pi.stepup.domain.dance.domain.AttendHistory;
import com.pi.stepup.domain.dance.domain.DanceMusic;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.dance.domain.Reservation;
import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class DanceRepositoryTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private DanceRepository danceRepository;

    private RandomDance randomDance;
    private RandomDance randomDance2;
    private RandomDance randomDance3;
    private Music music;
    private DanceMusic danceMusic;
    private User host;
    private User user;
    private Reservation reservation;
    private Reservation reservation2;
    private Reservation reservation3;
    private AttendHistory attend;
    private AttendHistory attend2;
    private Long pk = 1L;
    private final String title = "랜덤 플레이 댄스";
    private final String content = "함께 합시다";
    private final String mTitle = "ISTJ";
    private final String artist = "NCT DREAM";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm");
    private final String startAt1 = "2023-07-20 10:00";
    private final String startAt2 = "2024-09-20 10:00";
    private final String startAt3 = "2024-09-10 10:00";
    private final String endAt = "2024-09-30 10:00";


    @Test
    @BeforeEach
    public void init() {
        makeHost();
        makeUser();
        makeMusic();
    }

    public User makeHost() {
        host = User.builder()
                .id("hostId")
                .build();
        em.persist(host);
        return host;
    }

    public User makeUser() {
        user = User.builder()
                .id("userId")
                .build();
        em.persist(user);
        return user;
    }

    public Music makeMusic() {
        music = Music.builder()
                .title(mTitle)
                .artist(artist)
                .build();
        em.persist(music);
        return music;
    }

    public void makeDance() {
        randomDance = RandomDance.builder()
                .title(title)
                .content(content)
                .host(host)
                .startAt(LocalDateTime.parse(startAt1, formatter))
                .endAt(LocalDateTime.parse(endAt, formatter))
                .build();
    }

    public void makeDanceMusic() {
        danceMusic = DanceMusic.createDanceMusic(music);
        randomDance.addDanceMusicAndSetThis(danceMusic);
    }

    public void makeReservation() {
        reservation = Reservation.builder()
                .randomDance(randomDance)
                .user(user)
                .build();
    }

    public void makeDance2() {
        randomDance2 = RandomDance.builder()
                .title(title + 2)
                .content(content + 2)
                .host(host)
                .startAt(LocalDateTime.parse(startAt2, formatter))
                .endAt(LocalDateTime.parse(endAt, formatter))
                .build();
    }

    public void makeDance3() {
        randomDance3 = RandomDance.builder()
                .title(title + 3)
                .content(content + 3)
                .host(host)
                .startAt(LocalDateTime.parse(startAt3, formatter))
                .endAt(LocalDateTime.parse(endAt, formatter))
                .build();
    }

    public void makeDanceMusic2() {
        danceMusic = DanceMusic.createDanceMusic(music);
        randomDance2.addDanceMusicAndSetThis(danceMusic);
    }

    public void makeReservation2() {
        reservation2 = Reservation.builder()
                .randomDance(randomDance2)
                .user(user)
                .build();
    }

    public void makeReservation3() {
        reservation3 = Reservation.builder()
                .randomDance(randomDance3)
                .user(user)
                .build();
    }

    public void makeAttend() {
        attend = AttendHistory.builder()
                .randomDance(randomDance)
                .user(user)
                .build();
    }

    public void makeAttend2() {
        attend2 = AttendHistory.builder()
                .randomDance(randomDance2)
                .user(user)
                .build();
    }

    @Test
    @DisplayName("랜덤 플레이 댄스 개최 테스트")
    public void insertDanceTest() {
        makeDance();
        RandomDance saveDance = danceRepository.insert(randomDance);

        assertThat(saveDance).isEqualTo(randomDance);
        assertThat(saveDance.getRandomDanceId()).isEqualTo(randomDance.getRandomDanceId());
        assertThat(saveDance.getTitle()).isEqualTo(title);
        assertThat(saveDance.getContent()).isEqualTo(content);
    }

    @Test
    @DisplayName("개최 랜덤 플레이 댄스 하나 조회 테스트")
    public void findDanceTest() {
        makeDance();
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
        makeDance();
        em.persist(randomDance);

        assertThat(danceRepository.findOne(randomDance.getRandomDanceId())).isNotEmpty();

        danceRepository.delete(randomDance.getRandomDanceId());

        assertThat(danceRepository.findOne(randomDance.getRandomDanceId())).isEmpty();
    }

    @Test
    @DisplayName("개최 랜덤 플레이 댄스 노래 목록 조회 테스트")
    public void findAllDanceMusicTest() {
        makeDance();
        makeDanceMusic();
        em.persist(randomDance);

        List<DanceMusic> danceMusicList
                = danceRepository.findAllDanceMusic(randomDance.getRandomDanceId());

        assertThat(danceMusicList).isNotNull();
        assertThat(danceMusicList.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("개최 랜덤 플레이 댄스 목록 조회 테스트")
    public void findAllMyOpenDanceTest() {
        makeDance();
        makeDance2();
        em.persist(randomDance);
        em.persist(randomDance2);

        List<RandomDance> randomDanceList
                = danceRepository.findAllMyOpenDance(host.getId());

        assertThat(randomDanceList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("모든 랜덤 플레이 댄스 목록 조회 테스트")
    public void findAllDanceTest() {
        makeDance();
        makeDance2();
        em.persist(randomDance);
        em.persist(randomDance2);

        String keyword = "";
        List<RandomDance> randomDanceList
                = danceRepository.findAllDance(keyword);

        assertThat(randomDanceList.size()).isEqualTo(2);
        assertThat(randomDanceList.get(0).getStartAt())
                .isEqualTo(LocalDateTime.parse(startAt1, formatter));
        assertThat(randomDanceList.get(1).getStartAt())
                .isEqualTo(LocalDateTime.parse(startAt2, formatter));
    }

    @Test
    @DisplayName("진행예정 랜덤 플레이 댄스 목록 조회 테스트")
    public void findScheduledDanceTest() {
        makeDance();
        makeDance2();
        em.persist(randomDance);
        em.persist(randomDance2);

        List<RandomDance> randomDanceList
                = danceRepository.findScheduledDance("");

        assertThat(randomDanceList.size()).isEqualTo(1);
        assertThat(randomDanceList.get(0).getStartAt())
                .isEqualTo(LocalDateTime.parse(startAt2, formatter));
    }

    @Test
    @DisplayName("진행 중 랜덤 플레이 댄스 목록 조회 테스트")
    public void findInProgressDanceTest() {
        makeDance();
        makeDance2();
        em.persist(randomDance);
        em.persist(randomDance2);

        List<RandomDance> randomDanceList
                = danceRepository.findInProgressDance("");

        assertThat(randomDanceList.size()).isEqualTo(1);
        assertThat(randomDanceList.get(0).getStartAt())
                .isEqualTo(LocalDateTime.parse(startAt1, formatter));
    }

    @Test
    @DisplayName("랜덤 플레이 댄스 예약 테스트")
    public void insertReservationTest() {
        makeDance();
        em.persist(randomDance);
        makeReservation();
        Reservation saveReservation
                = danceRepository.insertReservation(reservation);

        assertThat(saveReservation).isEqualTo(reservation);
        assertThat(saveReservation.getReservationId()).isEqualTo(reservation.getReservationId());
        assertThat(saveReservation.getRandomDance()).isEqualTo(randomDance);
    }

    @Test
    @DisplayName("예약한 랜덤 플레이 댄스 하나 조회 테스트")
    public void findReservationTest() {
        makeDance2();
        em.persist(randomDance2);
        makeReservation2();
        em.persist(reservation2);

        Reservation findReservation1 = em.find(Reservation.class, reservation2.getReservationId());
        assertThat(findReservation1).isNotNull();
        Reservation findReservation2
                = danceRepository.findReservationByRandomDanceIdAndUserId
                (reservation2.getRandomDance().getRandomDanceId(),
                        reservation2.getUser().getUserId()).orElseThrow();
        assertThat(findReservation2.getReservationId()).isEqualTo(reservation2.getReservationId());
        assertThat(findReservation2).isNotNull();
        assertThat(findReservation1).isEqualTo(findReservation2);
    }

    @Test
    @DisplayName("예약한 랜덤 플레이 댄스 하나 조회 테스트2")
    public void findReservationTest2() {
        makeDance2();
        em.persist(randomDance2);
        makeReservation2();
        em.persist(reservation2);

        Reservation findReservation1 = em.find(Reservation.class, reservation2.getReservationId());
        Reservation findReservation2
                = danceRepository.findReservationByReservationIdAndRandomDanceId
                (reservation2.getReservationId(),
                        reservation2.getRandomDance().getRandomDanceId()).orElseThrow();

        assertThat(findReservation2.getReservationId()).isEqualTo(reservation2.getReservationId());
        assertThat(findReservation2).isNotNull();
        assertThat(findReservation1).isEqualTo(findReservation2);
    }

    @Test
    @DisplayName("랜덤 플레이 댄스 예약 취소")
    public void deleteReservationTest() {
        makeDance2();
        em.persist(randomDance2);
        makeReservation2();
        em.persist(reservation2);

        assertThat(
                danceRepository.findReservationByRandomDanceIdAndUserId
                        (reservation2.getRandomDance().getRandomDanceId(),
                                user.getUserId())).isNotEmpty();

        danceRepository.deleteReservation
                (reservation2.getRandomDance().getRandomDanceId(),
                        reservation2.getUser().getUserId());

        assertThat(
                danceRepository.findReservationByRandomDanceIdAndUserId
                        (reservation2.getRandomDance().getRandomDanceId(),
                                user.getUserId())).isEmpty();
    }

    @Test
    @DisplayName("랜덤 플레이 댄스 예약 목록 조회")
    public void findAllMyReservationTest() {
        makeDance2();
        em.persist(randomDance2);
        makeDance3();
        em.persist(randomDance3);
        makeReservation2();
        em.persist(reservation2);
        makeReservation3();
        em.persist(reservation3);

        List<Reservation> reservationList
                = danceRepository.findAllMyReservation(reservation2.getUser().getUserId());

        assertThat(reservationList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("랜덤 플레이 댄스 참여 테스트")
    public void insertAttendTest() {
        makeDance();
        em.persist(randomDance);
        makeAttend();
        AttendHistory saveAttend
                = danceRepository.insertAttend(attend);

        assertThat(saveAttend).isEqualTo(attend);
        assertThat(saveAttend.getAttendHistoryId()).isEqualTo(attend.getAttendHistoryId());
        assertThat(saveAttend.getRandomDance()).isEqualTo(randomDance);
    }

    @Test
    @DisplayName("참여한 랜덤 플레이 댄스 하나 조회 테스트")
    public void findAttendTest() {
        makeDance();
        em.persist(randomDance);
        makeAttend();
        em.persist(attend);

        AttendHistory findAttend1 = em.find(AttendHistory.class, attend.getAttendHistoryId());
        AttendHistory findAttend2
                = danceRepository.findAttendByRandomDanceIdAndUserId
                (attend.getRandomDance().getRandomDanceId(),
                        attend.getUser().getUserId()).orElseThrow();

        assertThat(findAttend2.getAttendHistoryId()).isEqualTo(attend.getAttendHistoryId());
        assertThat(findAttend2).isNotNull();
        assertThat(findAttend1).isEqualTo(findAttend2);
    }

    @Test
    @DisplayName("랜덤 플레이 댄스 참여 목록 조회")
    public void findAllMyAttendTest() {
        makeDance();
        em.persist(randomDance);
        makeAttend();
        em.persist(attend);
        makeDance2();
        em.persist(randomDance2);
        makeAttend2();
        em.persist(attend2);

        List<AttendHistory> attendList
                = danceRepository.findAllMyAttend(attend.getUser().getUserId());

        assertThat(attendList.size()).isEqualTo(2);
    }
}

