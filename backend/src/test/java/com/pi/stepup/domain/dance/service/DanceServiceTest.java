package com.pi.stepup.domain.dance.service;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pi.stepup.domain.dance.constant.DanceType;
import com.pi.stepup.domain.dance.constant.ProgressType;
import com.pi.stepup.domain.dance.dao.DanceRepository;
import com.pi.stepup.domain.dance.domain.AttendHistory;
import com.pi.stepup.domain.dance.domain.DanceMusic;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.dance.domain.Reservation;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceCreateRequestDto;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceSearchRequestDto;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceUpdateRequestDto;
import com.pi.stepup.domain.music.dao.MusicRepository;
import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.global.config.security.SecurityUtils;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class DanceServiceTest {

    @InjectMocks
    private DanceServiceImpl danceService;

    @Mock
    private DanceRepository danceRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    MusicRepository musicRepository;

    private RandomDance randomDance;
    private RandomDance randomDance2;
    private Music music;
    private Music music2;
    private DanceMusic danceMusic;
    private DanceMusic danceMusic2;
    private User host;
    private User user;
    private Reservation reservation;
    private Reservation reservation2;
    private AttendHistory attend;
    private AttendHistory attend2;
    private Long pk = 1L;
    private final Long pk2 = 2L;
    private final String title = "랜덤 플레이 댄스";
    private final String content = "함께 합시다";
    private final String startAt1 = "2023-07-20 10:00";
    private final String startAt2 = "2023-08-20 10:00";
    private final String endAt = "2023-08-30 10:00";
    private final String mTitle = "ISTJ";
    private final String artist = "NCT DREAM";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
        "yyyy-MM-dd HH:mm");
    private final DanceType type = DanceType.BASIC;
    private List<Long> danceMusicIdList = new ArrayList<>();
    private List<DanceMusic> danceMusicList = new ArrayList<>();
    private List<RandomDance> randomDanceList = new ArrayList<>();
    private DanceCreateRequestDto danceCreateRequestDto;
    private DanceUpdateRequestDto danceUpdateRequestDto;
    private DanceSearchRequestDto danceSearchRequestDto;

    @Test
    @BeforeEach
    public void init() {
        makeHost();
        makeUser();
        makeMusic();
        makeDance();
        makeDanceMusic();
    }

    public User makeHost() {
        host = User.builder()
            .id("hostId")
            .build();
        return host;
    }

    public User makeUser() {
        user = User.builder()
            .userId(pk2)
            .id("userId")
            .build();
        return user;
    }

    public Music makeMusic() {
        music = Music.builder()
            .musicId(pk)
            .title(mTitle)
            .artist(artist)
            .build();
        danceMusicIdList.add(pk);
        return music;
    }

//    public Music makeMusic2() {
//        music2 = Music.builder()
//            .musicId(pk2)
//            .title(mTitle + "2")
//            .artist(artist)
//            .build();
//        danceMusicIdList.add(pk2);
//        return music2;
//    }

    public void makeDance() {
        randomDance = RandomDance.builder()
            .randomDanceId(pk)
            .title(title)
            .content(content)
            .host(host)
            .startAt(LocalDateTime.parse(startAt1, formatter))
            .endAt(LocalDateTime.parse(endAt, formatter))
            .build();
        randomDanceList.add(randomDance);
    }

    public void makeDanceMusic() {
        danceMusic = DanceMusic.createDanceMusic(music);
        randomDance.addDanceMusicAndSetThis(danceMusic);
        danceMusicList.add(danceMusic);
    }

//    public void makeDanceMusic2() {
//        danceMusic2 = DanceMusic.createDanceMusic(music2);
//        randomDance.addDanceMusicAndSetThis(danceMusic2);
//        danceMusicList.add(danceMusic2);
//    }

    public void makeReservation() {
        reservation = Reservation.builder()
            .reservationId(pk)
            .randomDance(randomDance)
            .user(user)
            .build();
    }

    public void makeAttend() {
        attend = AttendHistory.builder()
            .attendHistoryId(pk)
            .randomDance(randomDance)
            .user(user)
            .build();
    }

    public void makeDanceCreateRequestDto() {
        danceCreateRequestDto
            = DanceCreateRequestDto.builder()
            .title(title)
            .content(content)
            .startAt(startAt1)
            .endAt(endAt)
            .danceType(String.valueOf(type))
            .maxUser(30)
            .hostId(host.getId())
            .danceMusicIdList(danceMusicIdList)
            .build();
    }

    public void makeDanceUpdateRequestDto() {
        danceUpdateRequestDto
            = DanceUpdateRequestDto.builder()
            .randomDanceId(pk)
            .title(title)
            .content(content)
            .startAt(startAt1)
            .endAt(endAt)
            .danceType(String.valueOf(type))
            .maxUser(30)
            .hostId(host.getId())
            .danceMusicIdList(danceMusicIdList)
            .build();
    }

    public void makeDanceSearchRequestDto() {
        danceSearchRequestDto
            = DanceSearchRequestDto.builder()
            .progressType(ProgressType.ALL.toString())
            .keyword("")
            .build();
    }

    //유효하지 않은 시간, 유효하지 않은 노래 개수
    @Test
    @DisplayName("랜덤 플레이 댄스 개최 테스트")
    public void createDanceTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMocked = mockStatic(SecurityUtils.class)) {
            securityUtilsMocked.when(SecurityUtils::getLoggedInUserId)
                .thenReturn(host.getId());

            when(this.userRepository.findById(host.getId()))
                .thenReturn(Optional.of(host));

            when(danceRepository.insert(any(RandomDance.class))).thenReturn(randomDance);
            when(musicRepository.findOne(any(Long.class))).thenReturn(Optional.of(music));

            makeDanceCreateRequestDto();
            assertThatNoException().isThrownBy(() -> danceService.create(danceCreateRequestDto));

            verify(danceRepository, times(1)).insert(any(RandomDance.class));
        }
    }

    //접근 권한
    @Test
    @DisplayName("랜덤 플레이 댄스 수정 테스트")
    public void updateDanceTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMocked = mockStatic(SecurityUtils.class)) {
            securityUtilsMocked.when(SecurityUtils::getLoggedInUserId)
                .thenReturn(host.getId());

            when(this.userRepository.findById(host.getId()))
                .thenReturn(Optional.of(host));
            when(danceRepository.findOne(any(Long.class))).thenReturn(Optional.of(randomDance));

            makeDanceUpdateRequestDto();

            assertThatNoException().isThrownBy(() -> danceService.update(danceUpdateRequestDto));
        }
    }

    //접근 권한
    @Test
    @DisplayName("랜덤 플레이 댄스 삭제 테스트")
    public void deleteDanceTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMocked = mockStatic(SecurityUtils.class)) {
            securityUtilsMocked.when(SecurityUtils::getLoggedInUserId)
                .thenReturn(host.getId());

            when(this.userRepository.findById(host.getId()))
                .thenReturn(Optional.of(host));

            when(danceRepository.findOne(any(Long.class))).thenReturn(Optional.of(randomDance));
            assertThatNoException().isThrownBy(() -> danceService.delete(pk));
            verify(danceRepository, times(1)).delete(pk);
        }
    }

    @Test
    @DisplayName("랜덤 플레이 댄스 노래 목록 테스트")
    public void readAllDanceMusicTest() {
        when(danceRepository.findOne(any(Long.class))).thenReturn(Optional.of(randomDance));
        when(danceRepository.findAllDanceMusic(any())).thenReturn(danceMusicList);
        when(musicRepository.findOne(any(Long.class))).thenReturn(Optional.of(music));

        assertThatNoException().isThrownBy(() -> danceService.readAllDanceMusic(pk));

        verify(danceRepository, times(1)).findAllDanceMusic(pk);
    }

    @Test
    @DisplayName("나의 개최 랜덤 플레이 댄스 목록 테스트")
    public void readAllMyOpenDanceTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMocked = mockStatic(SecurityUtils.class)) {
            securityUtilsMocked.when(SecurityUtils::getLoggedInUserId)
                .thenReturn(host.getId());

            when(this.userRepository.findById(host.getId()))
                .thenReturn(Optional.of(host));

            when(danceRepository.findAllMyOpenDance(any(String.class))).thenReturn(randomDanceList);

            assertThatNoException().isThrownBy(() -> danceService.readAllMyOpenDance());

            verify(danceRepository, times(1)).findAllMyOpenDance(host.getId());
        }
    }

    //조건별
    @Test
    @DisplayName("모든 랜덤 플레이 댄스 목록 테스트")
    public void readAllDanceTest() {
        when(danceRepository.findAllDance(any(String.class))).thenReturn(randomDanceList);

        makeDanceSearchRequestDto();
        assertThatNoException().isThrownBy(
            () -> danceService.readAllRandomDance(danceSearchRequestDto));

        verify(danceRepository, times(1)).findAllDance(danceSearchRequestDto.getKeyword());
    }

    //개최자가 예약 시
    //예약 중복
    @Test
    @DisplayName("랜덤 플레이 댄스 예약 테스트")
    public void createReservationTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMocked = mockStatic(SecurityUtils.class)) {
            securityUtilsMocked.when(SecurityUtils::getLoggedInUserId)
                .thenReturn(user.getId());

            when(this.userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

            when(danceRepository.findOne(any(Long.class))).thenReturn(Optional.of(randomDance));
            makeReservation();
            when(danceRepository.insertReservation(any(Reservation.class))).thenReturn(reservation);

            assertThatNoException().isThrownBy(() -> danceService.createReservation(pk));

            verify(danceRepository, times(1)).insertReservation(any(Reservation.class));
        }
    }

    //접근 권한
    @Test
    @DisplayName("랜덤 플레이 댄스 예약 취소 테스트")
    public void deleteReservationTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMocked = mockStatic(SecurityUtils.class)) {
            securityUtilsMocked.when(SecurityUtils::getLoggedInUserId)
                .thenReturn(user.getId());

            when(this.userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

            when(danceRepository.findOne(any(Long.class))).thenReturn(Optional.of(randomDance));

            makeReservation();
            when(danceRepository.findReservationByRandomDanceIdAndUserId
                (any(Long.class), any(Long.class)))
                .thenReturn(Optional.of(reservation));
            when(danceRepository.findReservationByReservationIdAndRandomDanceId
                (any(Long.class), any(Long.class)))
                .thenReturn(Optional.of(reservation));

            assertThatNoException().isThrownBy(() -> danceService.deleteReservation(pk));

            verify(danceRepository, times(1)).deleteReservation(pk, pk2);
        }
    }

    @Test
    @DisplayName("나의 예약 랜덤 플레이 댄스 목록 테스트")
    public void readAllMyReserveDanceTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMocked = mockStatic(SecurityUtils.class)) {
            securityUtilsMocked.when(SecurityUtils::getLoggedInUserId)
                .thenReturn(user.getId());

            when(this.userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

            makeReservation();

            assertThatNoException().isThrownBy(() -> danceService.readAllMyReserveDance());

            verify(danceRepository, times(1)).findAllMyReservation(pk2);
        }
    }

    //참여 중복
    @Test
    @DisplayName("랜덤 플레이 댄스 참여 테스트")
    public void createAttendTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMocked = mockStatic(SecurityUtils.class)) {
            securityUtilsMocked.when(SecurityUtils::getLoggedInUserId)
                .thenReturn(user.getId());

            when(this.userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

            when(danceRepository.findOne(any(Long.class))).thenReturn(Optional.of(randomDance));
            makeAttend();
            when(danceRepository.insertAttend(any(AttendHistory.class))).thenReturn(attend);

            assertThatNoException().isThrownBy(() -> danceService.createAttend(pk));

            verify(danceRepository, times(1)).insertAttend(any(AttendHistory.class));
        }
    }

    @Test
    @DisplayName("나의 참여 랜덤 플레이 댄스 목록 테스트")
    public void readAllMyAttendDanceTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMocked = mockStatic(SecurityUtils.class)) {
            securityUtilsMocked.when(SecurityUtils::getLoggedInUserId)
                .thenReturn(user.getId());

            when(this.userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

            makeAttend();

            assertThatNoException().isThrownBy(() -> danceService.readAllMyAttendDance());

            verify(danceRepository, times(1)).findAllMyAttend(pk2);
        }
    }

}
