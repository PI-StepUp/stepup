package com.pi.stepup.domain.music.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicSaveRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class MusicApiControllerTest {

    @Autowired
    private MusicApiController musicApiController;
    private MockMvc mockMvc;
    private Gson gson;
    private MusicSaveRequestDto music;

    @BeforeEach
    public void init() {
        gson = new Gson();
        mockMvc = MockMvcBuilders.standaloneSetup(musicApiController).build();

        music = new MusicSaveRequestDto(
            "spicy",
            "aespa",
            "",
            "url"
        );
    }


    @Test
    @DisplayName("musicApiController가 null이 아님을 테스트")
    public void setMusicApiControllerNotNullTest() {
        assertThat(musicApiController).isNotNull();
    }

    @Test
    @DisplayName("노래 추가 컨트롤러 테스트")
    public void createMusicControllerTest() throws Exception {
        String url = "/api/music";

//        String json = new ObjectMapper().writeValueAsString(music);
//        String json = "";

        final ResultActions actions = mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                .content(gson.toJson(music))
                .contentType(MediaType.APPLICATION_JSON)
        );

        actions.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("노래 한 곡 조회 테스트")
    @Transactional
    public void readOneMusicControllerTest() throws Exception {
        // insert
        StringBuilder url = new StringBuilder();

        url.append("/api/music");
        final ResultActions postAction = mockMvc.perform(
            MockMvcRequestBuilders.post(url.toString())
                .content(gson.toJson(music))
                .contentType(MediaType.APPLICATION_JSON)
        );

        // select
        Long musicId = 1L;
        url.append("/").append(musicId);
        final ResultActions getAction = mockMvc.perform(
            MockMvcRequestBuilders.get(url.toString())
        );

        getAction.andExpect(status().isOk()).andDo(print());
    }

}