package com.pi.stepup.domain.music.api;

import com.google.gson.Gson;
import com.pi.stepup.domain.music.domain.MusicApply;
import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicApplySaveRequestDto;
import com.pi.stepup.domain.music.service.MusicApplyService;
import com.pi.stepup.domain.user.domain.User;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MusicApplyApiControllerTest.class)
class MusicApplyApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MusicApplyService musicApplyService;

    private Gson gson;
    private MusicApplySaveRequestDto musicApplySaveRequestDto;
    private MusicApply musicApply;
    private User writer;

    @Test
    @BeforeEach
    public void init() {
        gson = new Gson();
        musicApplySaveRequestDto = MusicApplySaveRequestDto.builder()
                .artist("artist")
                .title("title")
                .content("content")
                .writerId("writer")
                .build();

        writer = User.builder()
                .id("writer")
                .build();

        musicApply = MusicApply.builder()
                .title(musicApplySaveRequestDto.getTitle())
                .artist(musicApplySaveRequestDto.getArtist())
                .content(musicApplySaveRequestDto.getContent())
                .writer(writer)
                .build();
    }


    @Test
    @DisplayName("노래 신청 등록 테스트")
    public void MusicApplyCreateControllerTest() throws Exception {
        when(musicApplyService.create(musicApplySaveRequestDto)).thenReturn(musicApply);

        String url = "/api/music/apply";
        final ResultActions postAction = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(musicApplySaveRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        postAction.andExpect(status().isCreated());
    }


}