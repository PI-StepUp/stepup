package com.pi.stepup.domain.music.api;

import com.google.gson.Gson;
import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicSaveRequestDto;
import com.pi.stepup.domain.music.dto.MusicResponseDto.MusicFindResponseDto;
import com.pi.stepup.domain.music.service.MusicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MusicApiController.class)
class MusicApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MusicService musicService;

    private Gson gson;
    private MusicSaveRequestDto musicSaveRequestDto;
    private Music music;

    @BeforeEach
    public void init() {
        gson = new Gson();
        musicSaveRequestDto = MusicSaveRequestDto.builder()
                .musicId(1L)
                .title("spicy")
                .artist("aespa")
                .answer("")
                .URL("url")
                .build();

        music = musicSaveRequestDto.toEntity();
    }

    @Test
    @DisplayName("노래 추가 컨트롤러 테스트")
    public void createMusicControllerTest() throws Exception {
        when(musicService.create(any())).thenReturn(music);

        String url = "/api/music";
        final ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(musicSaveRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        actions.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("노래 한 곡 조회 테스트")
    public void readOneMusicControllerTest() throws Exception {
        when(musicService.readOne(any())).thenReturn(Optional.of(music));

        long musicId = 1L;
        final ResultActions getAction = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/music/" + musicId)
        );

        getAction.andExpect(status().isOk()).andDo(print());
    }

    @Test
    @DisplayName("노래 목록 조회 테스트")
    public void readAllMusicControllerTest() throws Exception {
        when(musicService.readAll()).thenReturn(makeMusic());

        final ResultActions getAction = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/music")
        );

        getAction.andExpect(status().isOk()).andDo(print());
    }

    private List<MusicFindResponseDto> makeMusic() {
        List<MusicFindResponseDto> music = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Music tmp = Music.builder().title("title" + i).artist("artist" + (i + 1)).build();
            music.add(new MusicFindResponseDto(tmp));
        }
        return music;
    }
}