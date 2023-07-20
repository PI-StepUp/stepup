package com.pi.stepup.domain.music.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.pi.stepup.domain.music.domain.Music;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
class MusicApiControllerTest {

    @InjectMocks
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
        assertNotNull(musicApiController);
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

        final ResultActions actions = mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                .content(gson.toJson(music))
                .contentType(MediaType.APPLICATION_JSON)
        );

        actions.andExpect(status().isOk()).andDo(print());
    }

}