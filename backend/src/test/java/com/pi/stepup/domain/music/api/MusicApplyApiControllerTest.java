package com.pi.stepup.domain.music.api;

import com.google.gson.Gson;
import com.pi.stepup.domain.music.domain.MusicApply;
import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicApplySaveRequestDto;
import com.pi.stepup.domain.music.dto.MusicResponseDto.AllMusicApplyFindResponseDto;
import com.pi.stepup.domain.music.service.MusicApplyService;
import com.pi.stepup.domain.user.domain.User;
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

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MusicApplyApiControllerTest.class)
class MusicApplyApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MusicApplyService musicApplyService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private Gson gson;
    private MusicApplySaveRequestDto musicApplySaveRequestDto;
    private MusicApply musicApply;
    private User user;

    @Test
    @BeforeEach
    public void init() {
        gson = new Gson();
        makeMusicApplySaveRequestDto();
        makeMusicApply();
        makeMusicApplies();
        makeUser();
    }

    // TODO : 왜 403 forbidden..?
    @Test
    @DisplayName("노래 신청 등록 테스트")
    @WithMockUser
    public void MusicApplyCreateControllerTest() throws Exception {
        String url = "/api/music/apply";

        final ResultActions postAction = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(musicApplySaveRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        postAction.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("로그인 안한 사용자가 노래 신청 등록 할 경우 예외 처리 테스트")
    public void createMusicApplyNotLoginUserControllerTest() throws Exception {
        String url = "/api/music/apply";
        final ResultActions postAction = mockMvc.perform(
                MockMvcRequestBuilders.post(url).with(csrf())
                        .content(gson.toJson(musicApplySaveRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        postAction.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("노래 신청 목록 조회 테스트")
    public void MusicApplyReadAllControllerTest() throws Exception {
        String keyword = "";
        when(musicApplyService.readAllByKeyword(keyword)).thenReturn(makeMusicApplies());

        String url = "/api/music/apply?keyword=" + keyword;
        final ResultActions getAction = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
        );

        getAction.andExpect(status().isOk())
                .andDo(print());
    }

    private List<AllMusicApplyFindResponseDto> makeMusicApplies() {
        List<AllMusicApplyFindResponseDto> musicApplies = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            MusicApply tmp = MusicApply.builder()
                    .title("title" + i)
                    .artist("artist" + (i + 1))
                    .writer(user)
                    .build();
            musicApplies.add(new AllMusicApplyFindResponseDto(tmp));
        }
        return musicApplies;
    }

    private void makeMusicApply() {
        musicApply = MusicApply.builder()
                .title(musicApplySaveRequestDto.getTitle())
                .artist(musicApplySaveRequestDto.getArtist())
                .content(musicApplySaveRequestDto.getContent())
                .build();
    }

    private void makeUser() {
        user = User.builder()
                .id("user")
                .password("password")
                .build();
    }

    private void makeMusicApplySaveRequestDto() {
        musicApplySaveRequestDto = MusicApplySaveRequestDto.builder()
                .artist("artist")
                .title("title")
                .content("content")
                .writerId("user")
                .build();
    }
}