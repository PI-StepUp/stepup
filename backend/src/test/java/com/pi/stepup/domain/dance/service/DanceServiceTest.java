package com.pi.stepup.domain.dance.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pi.stepup.domain.dance.constant.DanceType;
import com.pi.stepup.domain.dance.constant.ProgressType;
import com.pi.stepup.domain.dance.dao.DanceRepository;
import com.pi.stepup.domain.dance.domain.DanceMusic;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.dance.domain.Reservation;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceCreateRequestDto;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceReserveRequestDto;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceSearchRequestDto;
import com.pi.stepup.domain.music.dao.MusicRepository;
import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.music.dto.MusicResponseDto.MusicFindResponseDto;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;


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

    private final String title = "랜덤 플레이 댄스";
    private final String content = "함께 합시다";
    private final String time = "2023-07-25 15:30";
    private final DanceType type = DanceType.BASIC;
    private final int max = 30;
    private final String url = "url";
    private final String id = "id";
    private final String nick = "nick";
    private final String mTitle = "ISTJ";
    private final String artist = "NCT DREAM";
    private final Long pk = 1L;
    private final Long pk2 = 2L;
    private User host;
    private User user;
    private Music music;
    private List<Long> musicIdList = new ArrayList<>();
    private DanceCreateRequestDto danceCreateRequestDto;
    private RandomDance randomDance;
    private DanceMusic danceMusic;
    private List<DanceMusic> danceMusicList = new ArrayList<>();
    private List<RandomDance> randomDanceList = new ArrayList<>();
    private DanceSearchRequestDto danceSearchRequestDto;
    private DanceReserveRequestDto danceReserveRequestDto;
    private Reservation reservation;

    @Test
    @BeforeEach
    public void init() {
        host = User.builder()
            .userId(pk)
            .id(id)
            .nickname(nick)
            .build();

        user = User.builder()
            .userId(pk2)
            .id(id+"2")
            .nickname(nick+"2")
            .build();

        music = Music.builder()
            .musicId(pk)
            .title(mTitle)
            .artist(artist)
            .build();

        musicIdList.add(pk);

        danceCreateRequestDto
            = DanceCreateRequestDto.builder().title(title)
            .content(content)
            .startAt(time)
            .endAt(time)
            .danceType(String.valueOf(type))
            .maxUser(max)
            .thumbnail(url)
            .hostId(host.getId())
            .danceMusicIdList(musicIdList)
            .build();

        randomDance = danceCreateRequestDto.toEntity(host);

        danceMusic = DanceMusic.createDanceMusic(music);
        danceMusicList.add(danceMusic);
//        randomDance.addDanceMusicAndSetThis(danceMusic);

        randomDanceList.add(randomDance);

        danceSearchRequestDto
            = DanceSearchRequestDto.builder()
            .progressType(ProgressType.ALL.toString())
            .keyword("")
            .build();

        danceReserveRequestDto
            = DanceReserveRequestDto.builder()
            .id(user.getId())
            .randomDanceId(pk)
            .build();
//
//        reservation = Reservation.builder()
//            .randomDance(randomDance)
//            .user(user)
//            .build();
    }

    @Test
    @DisplayName("랜덤 플레이 댄스 개최 테스트")
    public void createDanceTest() {
        when(danceRepository.insert(any(RandomDance.class))).thenReturn(randomDance);
        when(userRepository.findById(any(String.class))).thenReturn(Optional.of(host));
        when(musicRepository.findOne(any(Long.class))).thenReturn(Optional.of(music));
        danceService.create(danceCreateRequestDto);

        verify(danceRepository, times(1)).insert(any(RandomDance.class));
    }

    @Test
    @DisplayName("랜덤 플레이 댄스 삭제 테스트")
    public void deleteDanceTest() {
        when(danceRepository.findOne(any(Long.class))).thenReturn(Optional.of(randomDance));
        when(userRepository.findById(any(String.class))).thenReturn(Optional.of(host));
        danceService.delete(pk);

        verify(danceRepository, times(1)).delete(pk);
    }

    @Test
    @DisplayName("랜덤 플레이 댄스 노래 목록 테스트")
    public void readAllDanceMusicTest() {
        when(danceRepository.findOne(any(Long.class))).thenReturn(Optional.of(randomDance));
        when(danceRepository.findAllDanceMusic(any())).thenReturn(danceMusicList);
        when(musicRepository.findOne(any(Long.class))).thenReturn(Optional.of(music));
        danceService.readAllDanceMusic(pk);

        verify(danceRepository, times(1)).findAllDanceMusic(pk);
    }

    @Test
    @DisplayName("나의 개최 랜덤 플레이 댄스 목록 테스트")
    public void readAllMyDanceTest() {
        when(userRepository.findById(any(String.class))).thenReturn(Optional.of(host));
        when(danceRepository.findAllMyOpenDance(any(String.class))).thenReturn(randomDanceList);
        danceService.readAllMyOpenDance(id);

        verify(danceRepository, times(1)).findAllMyOpenDance(id);
    }

    @Test
    @DisplayName("모든 랜덤 플레이 댄스 목록 테스트")
    public void readAllDanceTest() {
        when(danceRepository.findAllDance(any(String.class))).thenReturn(randomDanceList);
        danceService.readAllRandomDance(danceSearchRequestDto);

        verify(danceRepository, times(1)).findAllDance(danceSearchRequestDto.getKeyword());
    }

    @Test
    @DisplayName("랜덤 플레이 댄스 예약 테스트")
    public void createReservationTest() {
        when(userRepository.findById(any(String.class))).thenReturn(Optional.of(user));
        when(danceRepository.findOne(danceReserveRequestDto.getRandomDanceId())).thenReturn(Optional.of(randomDance));
        when(danceRepository.findReservationByRandomDanceIdAndUserId(pk, user.getUserId())).thenReturn(Optional.of(reservation));
//        when(danceRepository.findReservationByRandomDanceIdAndUserId(pk, user.getUserId())).thenReturn(null);

        danceService.createReservation(danceReserveRequestDto);

        when(danceRepository.findReservationByRandomDanceIdAndUserId(pk, user.getUserId())).thenReturn(null);

//        verify(danceRepository, times(1)).insertReservation(reservation);
    }

//    @WithMockUser
//    @WithAnonymousUser

}
