package com.pi.stepup.domain.dance.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.pi.stepup.domain.dance.domain.AttendHistory;
import com.pi.stepup.domain.dance.domain.DanceMusic;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.dance.domain.Reservation;
import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.user.domain.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private RandomDance randomDance2;
    private Music music;
    private DanceMusic danceMusic;
    private User host;
    private User user;
    private Reservation reservation;
    private Reservation reservation2;
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
    private final String startAt2 = "2023-08-20 10:00";
    private final String endAt = "2023-08-30 10:00";


    @Test
    @BeforeEach
    public void init() {
        host = User.builder()
            .id("hostId")
            .build();
        em.persist(host);

        randomDance = RandomDance.builder()
            .title(title)
            .content(content)
            .host(host)
            .startAt(LocalDateTime.parse(startAt1, formatter))
            .endAt(LocalDateTime.parse(endAt, formatter))
            .build();

        randomDance2 = RandomDance.builder()
            .title(title + 2)
            .content(content + 2)
            .host(host)
            .startAt(LocalDateTime.parse(startAt2, formatter))
            .endAt(LocalDateTime.parse(endAt, formatter))
            .build();

        music = Music.builder()
            .title(mTitle)
            .artist(artist)
            .build();
        em.persist(music);

        danceMusic = DanceMusic.createDanceMusic(music);
        randomDance.addDanceMusicAndSetThis(danceMusic);
        randomDance2.addDanceMusicAndSetThis(danceMusic);

        user = User.builder()
            .id("userId")
            .build();
        em.persist(user);

        reservation = Reservation.builder()
            .randomDance(randomDance)
            .user(user)
            .build();

        reservation2 = Reservation.builder()
            .randomDance(randomDance2)
            .user(user)
            .build();

        attend = AttendHistory.builder()
            .randomDance(randomDance)
            .user(user)
            .build();

        attend2 = AttendHistory.builder()
            .randomDance(randomDance2)
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

        assertThat(danceRepository.findOne(randomDance.getRandomDanceId())).isNotEmpty();

        danceRepository.delete(randomDance.getRandomDanceId());

        assertThat(danceRepository.findOne(randomDance.getRandomDanceId())).isEmpty();
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
        em.persist(randomDance);
        em.persist(randomDance2);

        List<RandomDance> randomDanceList
            = danceRepository.findAllMyOpenDance(host.getId());

        assertThat(randomDanceList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("모든 랜덤 플레이 댄스 목록 조회 테스트")
    public void findAllDanceTest() {
        em.persist(randomDance);
        em.persist(randomDance2);

        List<RandomDance> randomDanceList
            = danceRepository.findAllDance("");

        assertThat(randomDanceList.size()).isEqualTo(2);
        assertThat(randomDanceList.get(0).getStartAt())
            .isEqualTo(LocalDateTime.parse(startAt1, formatter));
        assertThat(randomDanceList.get(1).getStartAt())
            .isEqualTo(LocalDateTime.parse(startAt2, formatter));
    }

    @Test
    @DisplayName("진행예정 랜덤 플레이 댄스 목록 조회 테스트")
    public void findScheduledDanceTest() {
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
        em.persist(randomDance);
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
    @DisplayName("예약한 랜덤 플레이 댄스 하나 조회 테스트2")
    public void findReservationTest2() {
        em.persist(randomDance);
        em.persist(reservation);

        Reservation findReservation1 = em.find(Reservation.class, reservation.getReservationId());
        Reservation findReservation2
            = danceRepository.findReservationByReservationIdAndRandomDanceId
                (reservation.getReservationId(), randomDance.getRandomDanceId())
            .orElseThrow();

        assertThat(findReservation2).isNotNull();
        assertThat(findReservation1).isEqualTo(findReservation2);
    }

    @Test
    @DisplayName("랜덤 플레이 댄스 예약 취소")
    public void deleteReservationTest() {
        em.persist(randomDance);
        em.persist(reservation);

        assertThat(
            danceRepository.findReservationByRandomDanceIdAndUserId(randomDance.getRandomDanceId(),
                user.getUserId())).isNotEmpty();

        danceRepository.deleteReservation
            (reservation.getRandomDance().getRandomDanceId(),
                reservation.getUser().getUserId());

        assertThat(
            danceRepository.findReservationByRandomDanceIdAndUserId(randomDance.getRandomDanceId(),
                user.getUserId())).isEmpty();
    }

    @Test
    @DisplayName("랜덤 플레이 댄스 예약 목록 조회")
    public void findAllMyReservationTest() {
        em.persist(randomDance);
        em.persist(randomDance2);
        em.persist(reservation);
        em.persist(reservation2);

        List<Reservation> reservationList
            = danceRepository.findAllMyReservation(reservation.getUser().getUserId());

        assertThat(reservationList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("랜덤 플레이 댄스 참여 테스트")
    public void insertAttendTest() {
        em.persist(randomDance);
        AttendHistory saveAttend
            = danceRepository.insertAttend(attend);

        assertThat(saveAttend).isEqualTo(attend);
        assertThat(saveAttend.getAttendHistoryId()).isEqualTo(pk);
        assertThat(saveAttend.getRandomDance()).isEqualTo(randomDance);
    }

    //TODO: 수정하기
//    @Test
//    @DisplayName("참여한 랜덤 플레이 댄스 하나 조회 테스트")
//    public void findAttendTest() {
//        em.persist(randomDance);
//        em.persist(attend);
//
//        AttendHistory findAttend1 = em.find(AttendHistory.class, attend.getAttendHistoryId());
//        AttendHistory findAttend2
//            = danceRepository.findAttendByRandomDanceIdAndUserId
//            (attend.getRandomDance().getRandomDanceId(), attend.getUser().getUserId()).orElseThrow();
//
////        assertThat(findAttend2).isNotNull();
////        assertThat(findAttend1).isEqualTo(findAttend2);
//    }

    @Test
    @DisplayName("랜덤 플레이 댄스 참여 목록 조회")
    public void findAllMyAttendTest() {
        em.persist(randomDance);
        em.persist(randomDance2);
        em.persist(attend);
        em.persist(attend2);

        List<AttendHistory> attendList
            = danceRepository.findAllMyAttend(attend.getUser().getUserId());

        assertThat(attendList.size()).isEqualTo(2);
    }

}
