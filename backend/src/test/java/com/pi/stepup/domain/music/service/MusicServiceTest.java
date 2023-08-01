package com.pi.stepup.domain.music.service;


import static com.pi.stepup.domain.music.constant.MusicExceptionMessage.MUSIC_DUPLICATED;
import static com.pi.stepup.domain.music.constant.MusicExceptionMessage.MUSIC_NOT_FOUND;
import static com.pi.stepup.domain.music.constant.MusicExceptionMessage.UNAUTHORIZED_USER_ACCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import com.pi.stepup.domain.music.dao.MusicRepository;
import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicSaveRequestDto;
import com.pi.stepup.domain.music.dto.MusicResponseDto.MusicFindResponseDto;
import com.pi.stepup.domain.music.exception.MusicDuplicatedException;
import com.pi.stepup.domain.music.exception.MusicNotFoundException;
import com.pi.stepup.domain.music.exception.UnauthorizedUserAccessException;
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
import org.springframework.transaction.annotation.Transactional;


@ExtendWith(MockitoExtension.class)
class MusicServiceTest {

    @InjectMocks
    private MusicServiceImpl musicService;

    @Mock
    private MusicRepository musicRepository;

    private MusicSaveRequestDto musicSaveRequestDto;
    private Music music;

    @Test
    @BeforeEach
    public void init() {
        makeMusicSaveRequestDto();
        music = musicSaveRequestDto.toEntity();
    }

    @Test
    @DisplayName("노래 추가 서비스 테스트")
    @Transactional
    public void createMusicServiceTest() {
        // TODO : ROLE_ADMIN return
        when(musicRepository.insert(any(Music.class))).thenReturn(music);

        Music result = musicService.create(musicSaveRequestDto);
        assertThat(result.getTitle()).isEqualTo(music.getTitle());
    }

    @Test
    @DisplayName("동일한 노래 추가 예외 처리 테스트")
    @Transactional
    public void createDuplicateMusicTest() {
        when(musicRepository.findByTitleAndArtist(any(), any())).thenReturn(
            Optional.ofNullable(music));

        assertThatThrownBy(() -> musicService.create(musicSaveRequestDto))
            .isInstanceOf(MusicDuplicatedException.class)
            .hasMessageContaining(MUSIC_DUPLICATED.getMessage());
    }

    @Test
    @DisplayName("관리자가 아닌 사용자가 노래 추가 예외 처리 테스트")
    @Transactional
    public void createMusicNotAdminTest() {
        // TODO : ROLE_USER return
//        when()

        assertThatThrownBy(() -> musicService.create(musicSaveRequestDto))
            .isInstanceOf(UnauthorizedUserAccessException.class)
            .hasMessageContaining(UNAUTHORIZED_USER_ACCESS.getMessage());
    }


    @Test
    @DisplayName("노래 한 곡 조회 테스트")
    public void readOneMusicServiceTest() {
        when(musicRepository.findOne(any())).thenReturn(Optional.of(music));

        MusicFindResponseDto result = musicService.readOne(music.getMusicId());

        assertThat(music.getTitle()).isEqualTo(result.getTitle());
    }

    @Test
    @DisplayName("없는 노래를 조회할 때 MUSIC_NOT_FOUND 예외 테스트")
    public void readOneMusicNotFoundTest() {
        when(musicRepository.findOne(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> musicService.readOne(music.getMusicId()))
            .isInstanceOf(MusicNotFoundException.class)
            .hasMessageContaining(MUSIC_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("노래 전체 목록 조회 테스트")
    public void readAllMusicServiceTest() {
        List<Music> makedMusic = makeMusic();
        String keyword = "";

        doReturn(makedMusic)
            .when(musicRepository)
            .findAll(keyword);

        List<MusicFindResponseDto> foundMusic = musicService.readAll(keyword);

        assertThat(makedMusic.size()).isEqualTo(foundMusic.size());
    }

    @Test
    @DisplayName("노래 키워드 조회 테스트")
    public void readAllByKeywordServiceTest() {
        List<Music> makedMusic = makeMusic();
        String keyword = "1";
        doReturn(makedMusic)
            .when(musicRepository)
            .findAll(keyword);

        List<MusicFindResponseDto> foundMusic = musicService.readAll(keyword);

        assertThat(makedMusic.size()).isEqualTo(foundMusic.size());
    }

    @Test
    @DisplayName("없는 노래를 삭제할 때 MUSIC_NOT_FOUND 예외 테스트")
    public void deleteOneMusicNotFoundTest() {
        when(musicRepository.findOne(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> musicService.delete(music.getMusicId()))
            .isInstanceOf(MusicNotFoundException.class)
            .hasMessageContaining(MUSIC_NOT_FOUND.getMessage());
    }

    private List<Music> makeMusic() {
        List<Music> music = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Music tmp = Music.builder().title("title" + i).artist("artist" + (i + 1)).build();
            music.add(tmp);
        }
        return music;
    }

    private void makeMusicSaveRequestDto() {
        musicSaveRequestDto = MusicSaveRequestDto.builder()
            .title("spicy")
            .artist("aespa")
            .answer("")
            .URL("url")
            .build();
    }
}