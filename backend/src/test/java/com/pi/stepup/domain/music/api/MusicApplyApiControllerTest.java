package com.pi.stepup.domain.music.api;

import static com.pi.stepup.domain.music.constant.MusicApplyApiUrl.CREATE_MUSIC_APPLY_URL;
import static com.pi.stepup.domain.music.constant.MusicApplyApiUrl.DELETE_MUSIC_APPLY_URL;
import static com.pi.stepup.domain.music.constant.MusicApplyApiUrl.READ_ALL_BY_ID_MUSIC_APPLY_URL;
import static com.pi.stepup.domain.music.constant.MusicApplyApiUrl.READ_ONE_MUSIC_APPLY;
import static com.pi.stepup.domain.music.constant.MusicExceptionMessage.MUSIC_APPLY_NOT_FOUND;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.pi.stepup.domain.music.domain.MusicApply;
import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicApplySaveRequestDto;
import com.pi.stepup.domain.music.dto.MusicResponseDto.MusicApplyFindResponseDto;
import com.pi.stepup.domain.music.exception.MusicApplyNotFoundException;
import com.pi.stepup.domain.music.service.MusicApplyService;
import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.global.util.jwt.JwtTokenProvider;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(controllers = MusicApplyApiController.class)
class MusicApplyApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MusicApplyService musicApplyService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private Gson gson;
    private MusicApplySaveRequestDto musicApplySaveRequestDto;
    private MusicApplyFindResponseDto musicApplyFindResponseDto;
    private MusicApply musicApply;
    private User user;

    @Test
    @BeforeEach
    public void init() {
        gson = new Gson();
        makeMusicApplySaveRequestDto();
        makeUser();
        makeMusicApply();
        makeMusicApplies();
        makeMusicApplyFindResponseDto();
    }

    @Test
    @DisplayName("노래 신청 등록 테스트")
    @WithMockUser
    public void createMusicApplyControllerTest() throws Exception {
        String url = CREATE_MUSIC_APPLY_URL.getUrl();

        final ResultActions postAction = mockMvc.perform(
            MockMvcRequestBuilders.post(url).with(csrf())
                .content(gson.toJson(musicApplySaveRequestDto))
                .contentType(MediaType.APPLICATION_JSON)
        );

        postAction.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("로그인 안한 사용자가 노래 신청 등록 할 경우 예외 처리 테스트")
    @WithAnonymousUser
    public void createMusicApplyNotLoginUserControllerTest() throws Exception {
        String url = CREATE_MUSIC_APPLY_URL.getUrl();
        final ResultActions postAction = mockMvc.perform(
            MockMvcRequestBuilders.post(url).with(csrf())
                .content(gson.toJson(musicApplySaveRequestDto))
                .contentType(MediaType.APPLICATION_JSON)
        );

        postAction.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("노래 신청 목록 조회 테스트")
    @WithMockUser
    public void readAllMusicApplyControllerTest() throws Exception {
        String keyword = "";
        when(musicApplyService.readAllByKeyword(keyword)).thenReturn(makeMusicApplies());

        String url = READ_ALL_BY_ID_MUSIC_APPLY_URL.getUrl() + keyword;
        final ResultActions getAction = mockMvc.perform(
            get(url)
        );

        getAction.andExpect(status().isOk()).andDo(print());
    }

    @Test
    @DisplayName("노래 신청 상세 조회 테스트")
    @WithMockUser
    public void readOneMusicControllerTest() throws Exception {
        when(musicApplyService.readOne(any())).thenReturn(musicApplyFindResponseDto);

        String url = READ_ONE_MUSIC_APPLY.getUrl() + musicApply.getMusicApplyId();
        final ResultActions getAction = mockMvc.perform(
            get(url)
        );

        getAction.andExpect(status().isOk()).andDo(print());
    }

    @Test
    @DisplayName("없는 노래 신청 상세 조회 예외 테스트")
    @WithMockUser
    public void readOneNotExistMusicApplyControllerTest() throws Exception {
        when(musicApplyService.readOne(any()))
            .thenThrow(new MusicApplyNotFoundException(MUSIC_APPLY_NOT_FOUND.getMessage()));

        String url = READ_ONE_MUSIC_APPLY.getUrl() + musicApply.getMusicApplyId();
        final ResultActions getAction = mockMvc.perform(
            get(url)
        );

        getAction.andExpect(status().isBadRequest())
            .andExpect(jsonPath("message").value(MUSIC_APPLY_NOT_FOUND.getMessage()));
    }

    @Test
    @DisplayName("로그인 안한 사용자가 노래 신청 상세 조회 예외 테스트")
    @WithAnonymousUser
    public void readOneMusicApplyNotLoginUserControllerTest() throws Exception {
        String url = READ_ONE_MUSIC_APPLY.getUrl() + musicApply.getMusicApplyId();
        final ResultActions getAction = mockMvc.perform(
            get(url)
        );

        getAction.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("노래 신청 삭제 테스트")
    @WithMockUser
    public void deleteMusicControllerTest() throws Exception {
        Long musicApplyId = 1L;

        final ResultActions deleteAction = mockMvc.perform(
            MockMvcRequestBuilders.delete(DELETE_MUSIC_APPLY_URL.getUrl() + musicApplyId)
                .with(csrf())
        );

        verify(musicApplyService, only()).delete(musicApplyId);
        deleteAction.andExpect(status().isOk());
    }

    @Test
    @DisplayName("없는 노래 신청 삭제 예외 테스트")
    @WithMockUser
    public void deleteNotExistMusicControllerTest() throws Exception {
        Long musicApplyId = 1L;
        doThrow(new MusicApplyNotFoundException(MUSIC_APPLY_NOT_FOUND.getMessage()))
            .when(musicApplyService)
            .delete(musicApplyId);

        final ResultActions deleteAction = mockMvc.perform(
            MockMvcRequestBuilders.delete(DELETE_MUSIC_APPLY_URL.getUrl() + musicApplyId)
                .with(csrf())
        );

        verify(musicApplyService, only()).delete(musicApplyId);
        deleteAction.andExpect(status().isBadRequest())
            .andExpect(jsonPath("message").value(MUSIC_APPLY_NOT_FOUND.getMessage()));
    }

    @Test
    @DisplayName("비인증된 사용자 노래 신청 삭제 예외 테스트")
    @WithAnonymousUser
    public void notLoginUserDeleteMusicControllerTest() throws Exception {
        Long musicApplyId = 1L;

        final ResultActions deleteAction = mockMvc.perform(
            MockMvcRequestBuilders.delete(DELETE_MUSIC_APPLY_URL.getUrl() + musicApplyId)
                .with(csrf())
        );

        deleteAction.andExpect(status().isUnauthorized());
    }

    // TODO : 노래 신청 삭제 403
    //  등록자가 아닌 사용자가 삭제 요청


    private List<MusicApplyFindResponseDto> makeMusicApplies() {
        List<MusicApplyFindResponseDto> musicApplies = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            MusicApply tmp = MusicApply.builder()
                .title("title" + i)
                .artist("artist" + (i + 1))
                .writer(user)
                .build();
            musicApplies.add(new MusicApplyFindResponseDto(tmp, 1));
        }
        return musicApplies;
    }

    private void makeMusicApply() {
        musicApply = MusicApply.builder()
            .musicApplyId(1L)
            .title(musicApplySaveRequestDto.getTitle())
            .artist(musicApplySaveRequestDto.getArtist())
            .content(musicApplySaveRequestDto.getContent())
            .writer(user)
            .build();
    }

    private void makeUser() {
        user = User.builder()
            .id("j3beom")
            .password("password")
            .build();
    }

    private void makeMusicApplySaveRequestDto() {
        musicApplySaveRequestDto = MusicApplySaveRequestDto.builder()
            .artist("artist")
            .title("title")
            .content("content")
            .build();
    }

    private void makeMusicApplyFindResponseDto() {
        musicApplyFindResponseDto = MusicApplyFindResponseDto.builder()
            .musicApply(musicApply)
            .canHeart(1)
            .build();
    }
}