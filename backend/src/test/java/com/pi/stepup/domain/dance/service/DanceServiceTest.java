package com.pi.stepup.domain.dance.service;

import static com.pi.stepup.domain.dance.constant.DanceExceptionMessage.ATTEND_DUPLICATED;
import static com.pi.stepup.domain.dance.constant.DanceExceptionMessage.DANCE_DELETE_FORBIDDEN;
import static com.pi.stepup.domain.dance.constant.DanceExceptionMessage.DANCE_INVALID_MUSIC;
import static com.pi.stepup.domain.dance.constant.DanceExceptionMessage.DANCE_INVALID_TIME;
import static com.pi.stepup.domain.dance.constant.DanceExceptionMessage.DANCE_NOT_FOUND;
import static com.pi.stepup.domain.dance.constant.DanceExceptionMessage.DANCE_UPDATE_FORBIDDEN;
import static com.pi.stepup.domain.dance.constant.DanceExceptionMessage.RESERVATION_DUPLICATED;
import static com.pi.stepup.domain.dance.constant.DanceExceptionMessage.RESERVATION_IMPOSSIBLE;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
import com.pi.stepup.domain.dance.exception.AttendDuplicatedException;
import com.pi.stepup.domain.dance.exception.DanceBadRequestException;
import com.pi.stepup.domain.dance.exception.DanceForbiddenException;
import com.pi.stepup.domain.dance.exception.ReservationDuplicatedException;
import com.pi.stepup.domain.music.dao.MusicAnswerRepository;
import com.pi.stepup.domain.music.dao.MusicRepository;
import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.music.domain.MusicAnswer;
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

    @Mock
    MusicAnswerRepository musicAnswerRepository;

    private RandomDance randomDance;
    private RandomDance randomDance2;
    private Music music;
    private Music music2;
    private DanceMusic danceMusic;
    private DanceMusic danceMusic2;
    private User host;
    private User user;
    private User user2;
    private Reservation reservation;
    private AttendHistory attend;
    private final Long pk = 1L;
    private final Long pk2 = 2L;
    private final Long pk3 = 3L;
    private final String title = "랜덤 플레이 댄스";
    private final String content = "함께 합시다";
    private final String startAt1 = "2023-07-20 10:00";
    private final String startAt2 = "2023-08-20 10:00";
    private final String endAt = "2023-08-30 10:00";
    private final String mTitle = "ISTJ";
    private final String artist = "NCT DREAM";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
        "yyyy-MM-dd HH:mm");
    private final DanceType type = DanceType.BASIC;
    private List<Long> danceMusicIdList = new ArrayList<>();
    private List<Long> danceMusicIdExceptionList = new ArrayList<>();
    private List<DanceMusic> danceMusicList = new ArrayList<>();
    private List<DanceMusic> danceMusicExceptionList = new ArrayList<>();
    private List<RandomDance> randomDanceList = new ArrayList<>();
    private DanceCreateRequestDto danceCreateRequestDto;
    private DanceUpdateRequestDto danceUpdateRequestDto;
    private DanceSearchRequestDto danceSearchRequestDto;

    @Test
    @BeforeEach
    public void init() {
        makeHost();
        makeUser();
        makeUser2();
        makeMusic();
        makeMusic2();
        makeDance();
        makeDance2();
        makeDanceMusic();
        makeDanceMusic2();
        makeDanceMusicException();
    }

    public void makeHost() {
        host = User.builder()
            .id("hostId")
            .build();
    }

    public void makeUser() {
        user = User.builder()
            .userId(pk2)
            .id("userId")
            .build();
    }

    public void makeUser2() {
        user2 = User.builder()
            .userId(pk3)
            .id("userId2")
            .build();
    }

    public void makeMusic() {
        music = Music.builder()
            .musicId(pk)
            .title(mTitle)
            .artist(artist)
            .build();
        danceMusicIdList.add(pk);
        danceMusicIdExceptionList.add(pk);
    }

    public void makeMusic2() {
        music2 = Music.builder()
            .musicId(pk2)
            .title(mTitle + "2")
            .artist(artist)
            .build();
        danceMusicIdList.add(pk2);
    }

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

    public void makeDance2() {
        randomDance2 = RandomDance.builder()
            .randomDanceId(pk2)
            .title(title)
            .content(content)
            .host(host)
            .startAt(LocalDateTime.parse(startAt2, formatter))
            .endAt(LocalDateTime.parse(endAt, formatter))
            .build();
        randomDanceList.add(randomDance2);
    }

    public void makeDanceMusic() {
        danceMusic = DanceMusic.createDanceMusic(music);
        randomDance.addDanceMusicAndSetThis(danceMusic);
        randomDance2.addDanceMusicAndSetThis(danceMusic);
        danceMusicList.add(danceMusic);
    }

    public void makeDanceMusicException() {
        danceMusic = DanceMusic.createDanceMusic(music);
        randomDance.addDanceMusicAndSetThis(danceMusic);
        danceMusicExceptionList.add(danceMusic);
    }

    public void makeDanceMusic2() {
        danceMusic2 = DanceMusic.createDanceMusic(music2);
        randomDance.addDanceMusicAndSetThis(danceMusic2);
        randomDance2.addDanceMusicAndSetThis(danceMusic2);
        danceMusicList.add(danceMusic2);
    }

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

    public void makeDanceCreateExceptionTimeRequestDto() {
        danceCreateRequestDto
            = DanceCreateRequestDto.builder()
            .title(title)
            .content(content)
            .startAt(endAt)
            .endAt(startAt1)
            .danceType(String.valueOf(type))
            .maxUser(30)
            .hostId(host.getId())
            .danceMusicIdList(danceMusicIdList)
            .build();
    }

    public void makeDanceCreateExceptionMusicRequestDto() {
        danceCreateRequestDto
            = DanceCreateRequestDto.builder()
            .title(title)
            .content(content)
            .startAt(startAt1)
            .endAt(endAt)
            .danceType(String.valueOf(type))
            .maxUser(30)
            .hostId(host.getId())
            .danceMusicIdList(danceMusicIdExceptionList)
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

    public void makeDanceAllSearchRequestDto() {
        danceSearchRequestDto
            = DanceSearchRequestDto.builder()
            .progressType(ProgressType.ALL.toString())
            .keyword("")
            .build();
    }

    public void makeDanceScheduledSearchRequestDto() {
        danceSearchRequestDto
            = DanceSearchRequestDto.builder()
            .progressType(ProgressType.SCHEDULED.toString())
            .keyword("")
            .build();
    }

    public void makeDanceInProgressSearchRequestDto() {
        danceSearchRequestDto
            = DanceSearchRequestDto.builder()
            .progressType(ProgressType.IN_PROGRESS.toString())
            .keyword("")
            .build();
    }

    @Test
    @DisplayName("랜덤 플레이 댄스 하나 조회 예외 테스트")
    public void readDanceTest() {
        when(danceRepository.findOne(any(Long.class))).thenReturn(Optional.empty());

        assertThatThrownBy(()
            -> danceService.delete(any(Long.class)))
            .isInstanceOf(DanceBadRequestException.class)
            .hasMessageContaining(DANCE_NOT_FOUND.getMessage());

        assertThatThrownBy(()
            -> danceService.readAllDanceMusic(any(Long.class)))
            .isInstanceOf(DanceBadRequestException.class)
            .hasMessageContaining(DANCE_NOT_FOUND.getMessage());
    }

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

    @Test
    @DisplayName("랜덤 플레이 댄스 개최 예외 테스트 - 유효하지 않은 시간인 경우")
    public void createDanceExceptionTimeTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMocked = mockStatic(SecurityUtils.class)) {
            securityUtilsMocked.when(SecurityUtils::getLoggedInUserId)
                .thenReturn(host.getId());

            when(this.userRepository.findById(host.getId()))
                .thenReturn(Optional.of(host));

            makeDanceCreateExceptionTimeRequestDto();
            assertThatThrownBy(()
                -> danceService.create(danceCreateRequestDto))
                .isInstanceOf(DanceBadRequestException.class)
                .hasMessageContaining(DANCE_INVALID_TIME.getMessage());

            verify(danceRepository, times(0)).insert(any(RandomDance.class));
        }
    }

    @Test
    @DisplayName("랜덤 플레이 댄스 개최 예외 테스트 - 유효하지 않은 노래 개수인 경우")
    public void createDanceExceptionMusicTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMocked = mockStatic(SecurityUtils.class)) {
            securityUtilsMocked.when(SecurityUtils::getLoggedInUserId)
                .thenReturn(host.getId());

            when(this.userRepository.findById(host.getId()))
                .thenReturn(Optional.of(host));

            makeDanceCreateExceptionMusicRequestDto();
            assertThatThrownBy(()
                -> danceService.create(danceCreateRequestDto))
                .isInstanceOf(DanceBadRequestException.class)
                .hasMessageContaining(DANCE_INVALID_MUSIC.getMessage());

            verify(danceRepository, times(0)).insert(any(RandomDance.class));
        }
    }

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

    @Test
    @DisplayName("랜덤 플레이 댄스 수정 예외 테스트 - 접근 권한")
    public void updateDanceExceptionTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMocked = mockStatic(SecurityUtils.class)) {
            securityUtilsMocked.when(SecurityUtils::getLoggedInUserId)
                .thenReturn(user.getId());

            when(this.userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

            makeDanceUpdateRequestDto();

            assertThatThrownBy(()
                -> danceService.update(danceUpdateRequestDto))
                .isInstanceOf(DanceForbiddenException.class)
                .hasMessageContaining(DANCE_UPDATE_FORBIDDEN.getMessage());
        }
    }

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
    @DisplayName("랜덤 플레이 댄스 삭제 예외 테스트 - 접근 권한")
    public void deleteDanceExceptionTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMocked = mockStatic(SecurityUtils.class)) {
            securityUtilsMocked.when(SecurityUtils::getLoggedInUserId)
                .thenReturn(user.getId());

            when(this.userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

            when(danceRepository.findOne(any(Long.class))).thenReturn(Optional.of(randomDance));
            assertThatThrownBy(()
                -> danceService.delete(pk))
                .isInstanceOf(DanceForbiddenException.class)
                .hasMessageContaining(DANCE_DELETE_FORBIDDEN.getMessage());

            verify(danceRepository, times(0)).delete(pk);
        }
    }

    @Test
    @DisplayName("랜덤 플레이 댄스 노래 목록 테스트")
    public void readAllDanceMusicTest() {
        when(danceRepository.findOne(any(Long.class))).thenReturn(Optional.of(randomDance));
        when(danceRepository.findAllDanceMusic(any())).thenReturn(danceMusicList);
        when(musicRepository.findOne(any(Long.class))).thenReturn(Optional.of(music));
        when(musicAnswerRepository.findById(any())).thenReturn(
            Optional.of(MusicAnswer.builder().build()));

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

    @Test
    @DisplayName("모든 랜덤 플레이 댄스 목록 테스트 - ALL")
    public void readAllDanceTest() {
        when(danceRepository.findAllDance(any(String.class))).thenReturn(randomDanceList);

        makeDanceAllSearchRequestDto();
        assertThatNoException().isThrownBy
            (() -> danceService.readAllRandomDance(danceSearchRequestDto));

        verify(danceRepository, times(1)).findAllDance(danceSearchRequestDto.getKeyword());
        verify(danceRepository, times(0)).findScheduledDance(danceSearchRequestDto.getKeyword());
        verify(danceRepository, times(0)).findInProgressDance(danceSearchRequestDto.getKeyword());
    }

    @Test
    @DisplayName("모든 랜덤 플레이 댄스 목록 테스트 - SCHEDULED")
    public void readScheduledDanceTest() {
        when(danceRepository.findScheduledDance((any(String.class)))).thenReturn(randomDanceList);

        makeDanceScheduledSearchRequestDto();
        assertThatNoException().isThrownBy
            (() -> danceService.readAllRandomDance(danceSearchRequestDto));

        verify(danceRepository, times(0)).findAllDance(danceSearchRequestDto.getKeyword());
        verify(danceRepository, times(1)).findScheduledDance(danceSearchRequestDto.getKeyword());
        verify(danceRepository, times(0)).findInProgressDance(danceSearchRequestDto.getKeyword());
    }

    @Test
    @DisplayName("모든 랜덤 플레이 댄스 목록 테스트 - IN_PROGRESS")
    public void readInProgressDanceTest() {
        when(danceRepository.findInProgressDance((any(String.class)))).thenReturn(randomDanceList);

        makeDanceInProgressSearchRequestDto();
        assertThatNoException().isThrownBy
            (() -> danceService.readAllRandomDance(danceSearchRequestDto));

        verify(danceRepository, times(0)).findAllDance(danceSearchRequestDto.getKeyword());
        verify(danceRepository, times(0)).findScheduledDance(danceSearchRequestDto.getKeyword());
        verify(danceRepository, times(1)).findInProgressDance(danceSearchRequestDto.getKeyword());
    }

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

    @Test
    @DisplayName("랜덤 플레이 댄스 예약 예외 테스트 - 개최자가 예약 시도")
    public void createReservationExceptionTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMocked = mockStatic(SecurityUtils.class)) {
            securityUtilsMocked.when(SecurityUtils::getLoggedInUserId)
                .thenReturn(host.getId());

            when(this.userRepository.findById(host.getId()))
                .thenReturn(Optional.of(host));

            when(danceRepository.findOne(any(Long.class))).thenReturn(Optional.of(randomDance));
            makeReservation();

            assertThatThrownBy(()
                -> danceService.createReservation(pk))
                .isInstanceOf(ReservationDuplicatedException.class)
                .hasMessageContaining(RESERVATION_IMPOSSIBLE.getMessage());

            verify(danceRepository, times(0)).insertReservation(any(Reservation.class));
        }
    }

    @Test
    @DisplayName("랜덤 플레이 댄스 예약 예외 테스트 - 예약 중복")
    public void createReservationDupExceptionTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMocked = mockStatic(SecurityUtils.class)) {
            securityUtilsMocked.when(SecurityUtils::getLoggedInUserId)
                .thenReturn(user.getId());

            when(this.userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

            when(danceRepository.findOne(any(Long.class))).thenReturn(Optional.of(randomDance));

            makeReservation();
            when(danceRepository.findReservationByRandomDanceIdAndUserId
                (any(Long.class), any(Long.class))).thenReturn(Optional.of(reservation));
            makeReservation();

            assertThatThrownBy(()
                -> danceService.createReservation(pk))
                .isInstanceOf(ReservationDuplicatedException.class)
                .hasMessageContaining(RESERVATION_DUPLICATED.getMessage());

            verify(danceRepository, times(0)).insertReservation(any(Reservation.class));
        }
    }

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

    //TODO
//    @Test
//    @DisplayName("랜덤 플레이 댄스 예약 취소 예외 테스트 - 접근 권한")
//    public void deleteReservationExceptionTest() {
//        try (MockedStatic<SecurityUtils> securityUtilsMocked = mockStatic(SecurityUtils.class)) {
//            securityUtilsMocked.when(SecurityUtils::getLoggedInUserId)
//                .thenReturn(user2.getId());
//
//            when(this.userRepository.findById(user2.getId()))
//                .thenReturn(Optional.of(user2));
//
//            when(danceRepository.findOne(any(Long.class))).thenReturn(Optional.of(randomDance));
//
//            makeReservation();
//            when(danceRepository.findReservationByRandomDanceIdAndUserId
//                (any(Long.class), any(Long.class)))
//                .thenReturn(Optional.of(reservation));
//            when(danceRepository.findReservationByReservationIdAndRandomDanceId
//                (any(Long.class), any(Long.class)))
//                .thenReturn(Optional.of(reservation));
//
//            when(danceRepository.findReservationByRandomDanceIdAndUserId
//                (reservation.getReservationId(), user2.getUserId()))
//                .thenReturn(Optional.empty());
//
//            assertThatThrownBy(()
//                -> danceService.deleteReservation(pk))
//                .isInstanceOf(ReservationForbiddenException.class)
//                .hasMessageContaining(RESERVATION_DELETE_FORBIDDEN.getMessage());
//        }
//    }

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
    @DisplayName("랜덤 플레이 댄스 참여 예외 테스트 - 참여 중복")
    public void createAttendDupExceptionTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMocked = mockStatic(SecurityUtils.class)) {
            securityUtilsMocked.when(SecurityUtils::getLoggedInUserId)
                .thenReturn(user.getId());

            when(this.userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

            when(danceRepository.findOne(any(Long.class))).thenReturn(Optional.of(randomDance));

            makeAttend();
            when(danceRepository.findAttendByRandomDanceIdAndUserId
                (any(Long.class), any(Long.class))).thenReturn(Optional.of(attend));
            makeAttend();

            assertThatThrownBy(()
                -> danceService.createAttend(pk))
                .isInstanceOf(AttendDuplicatedException.class)
                .hasMessageContaining(ATTEND_DUPLICATED.getMessage());

            verify(danceRepository, times(0)).insertAttend(any(AttendHistory.class));
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
