package com.pi.stepup.domain.dance.service;

import com.pi.stepup.domain.dance.constant.DanceType;
import com.pi.stepup.domain.dance.dao.DanceRepository;
import com.pi.stepup.domain.dance.domain.DanceMusic;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceCreateRequestDto;
import com.pi.stepup.domain.music.dao.MusicRepository;
import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


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

    private User user;
    private Music music;
    private List<Long> musicIdList = new ArrayList<>();
    private DanceCreateRequestDto danceCreateRequestDto;
    private RandomDance randomDance;
    private Long randomDanceId = 1L;
    private DanceMusic danceMusic;
    private List<DanceMusic> danceMusicList = new ArrayList<>();

    @Test
    @BeforeEach
    public void init() {
        user = User.builder()
                .id(id)
                .nickname(nick)
                .build();

        music = Music.builder()
                .title(mTitle)
                .artist(artist)
                .build();

        musicIdList.add(1L);

        danceCreateRequestDto
                = DanceCreateRequestDto.builder().title(title)
                .content(content)
                .startAt(time)
                .endAt(time)
                .danceType(type)
                .maxUser(max)
                .thumbnail(url)
                .hostId(user.getId())
                .danceMusicIdList(musicIdList)
                .build();

        randomDance = danceCreateRequestDto.toEntity(user);

        danceMusic = DanceMusic.createDanceMusic(music);
        danceMusicList.add(danceMusic);
        randomDance.addDanceMusicAndSetThis(danceMusic);
    }

    @Test
    @DisplayName("랜덤 플레이 댄스 개최 테스트")
    public void createDanceTest() {
        when(danceRepository.insert(any(RandomDance.class))).thenReturn(randomDance);
        when(userRepository.findById(any(String.class))).thenReturn(Optional.of(user));
        when(musicRepository.findOne(any(Long.class))).thenReturn(Optional.of(music));
        danceService.create(danceCreateRequestDto);

        verify(danceRepository, times(1)).insert(any(RandomDance.class));
    }

    @Test
    @DisplayName("랜덤 플레이 댄스 삭제 테스트")
    public void deleteDanceTest() {
        when(danceRepository.findOne(any(Long.class))).thenReturn(Optional.of(randomDance));
        when(userRepository.findById(any(String.class))).thenReturn(Optional.of(user));

        danceService.delete(randomDanceId);
        verify(danceRepository, times(1)).delete(randomDanceId);
    }

//    @Test
//    @DisplayName("랜덤 플레이 댄스 노래 목록 테스트")
//    public void readAllDanceMusicTest() {
//        when(danceRepository.findOne(any(Long.class))).thenReturn(Optional.of(randomDance));
//        when(danceRepository.findAllDanceMusic(any())).thenReturn(danceMusicList);
//
//        List<MusicFindResponseDto> musicFindResponseDtoList
//                = danceService.readAllDanceMusic(randomDanceId);
//        assertThat(danceMusicList.size()).isEqualTo(musicFindResponseDtoList.size());
//
//        verify(danceRepository, times(1)).findAllDanceMusic(randomDanceId);
//    }

}
