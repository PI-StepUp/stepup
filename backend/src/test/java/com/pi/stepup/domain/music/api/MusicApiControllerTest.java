package com.pi.stepup.domain.music.api;

import com.google.gson.Gson;
import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicSaveRequestDto;
import com.pi.stepup.domain.music.dto.MusicResponseDto.MusicFindResponseDto;
import com.pi.stepup.domain.music.service.MusicService;
import com.pi.stepup.global.util.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MusicApiController.class
//,
//        excludeFilters = {
//                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class
//                )}
)
class MusicApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MusicService musicService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider; // jwtTokenProvider 주입받기 실패했다고 떠서 추가

    private Gson gson;
    private MusicSaveRequestDto musicSaveRequestDto;
    private MusicFindResponseDto musicFindResponseDto;
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

        musicFindResponseDto = MusicFindResponseDto.builder().music(music).build();
    }

    @Test
    @DisplayName("노래 추가 컨트롤러 테스트")
    @WithMockUser
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
    @WithMockUser
    public void readOneMusicControllerTest() throws Exception {
        when(musicService.readOne(any())).thenReturn(musicFindResponseDto);

        long musicId = 1L;
        final ResultActions getAction = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/music/" + musicId)
        );

        getAction.andExpect(status().isOk()).andDo(print());
    }

    @Test
    @DisplayName("노래 목록 조회 테스트")
    @WithMockUser
    public void readAllMusicControllerTest() throws Exception {
        String keyword = "";
        when(musicService.readAll(keyword)).thenReturn(makeMusic());

        final ResultActions getAction = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/music?keyword=")
        );

        getAction.andExpect(status().isOk())
                .andExpect(jsonPath("data").isNotEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("노래 목록 키워드 조회 테스트")
    @WithMockUser
    public void readAllByKeywordMusicControllerTest() throws Exception {
        String keyword = "1";
        when(musicService.readAll(keyword)).thenReturn(keywordMusic(keyword));

        final ResultActions getAction = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/music?keyword=" + keyword)
        );

        getAction.andExpect(status().isOk())
                .andExpect(jsonPath("data[3]").doesNotExist())
                .andDo(print());
    }

    @Test
    @DisplayName("노래 삭제 테스트")
    @WithMockUser
    public void deleteMusicControllerTest() throws Exception {
        Long musicId = 1L;

        final ResultActions deleteAction = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/music/" + musicId)
        );

        verify(musicService, only()).delete(musicId);
        deleteAction.andExpect(status().isOk());
    }

    private List<MusicFindResponseDto> makeMusic() {
        List<MusicFindResponseDto> music = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Music tmp = Music.builder().title("title" + i).artist("artist" + (i + 1)).build();
            music.add(new MusicFindResponseDto(tmp));
        }
        return music;
    }

    private List<MusicFindResponseDto> keywordMusic(String keyword) {
        List<MusicFindResponseDto> music = makeMusic();
        List<MusicFindResponseDto> result = new ArrayList<>();
        for (MusicFindResponseDto dto : music) {
            if (dto.getTitle().contains(keyword) || dto.getArtist().contains(keyword)) {
                result.add(dto);
            }
        }
        return result;
    }

}