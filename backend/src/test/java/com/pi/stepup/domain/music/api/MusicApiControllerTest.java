package com.pi.stepup.domain.music.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.pi.stepup.domain.music.domain.Music;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

    @ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class MusicApiControllerTest {

    @Autowired
    private MusicApiController musicApiController;

    private MockMvc mockMvc;
    private Gson gson;

    @BeforeEach
    public void init() {
        gson = new Gson();
        mockMvc = MockMvcBuilders.standaloneSetup(musicApiController).build();
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

        Music music = Music.builder()
            .title("spicy")
            .artist("aespa")
            .answer("")
            .URL("url")
            .build();

//        String json = new ObjectMapper().writeValueAsString(music);
//        String json = "";

        final ResultActions actions = mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                .content(gson.toJson(music))
                .contentType(MediaType.APPLICATION_JSON)
        );

        actions.andExpect(status().isCreated());
    }

}