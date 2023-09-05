package com.pi.stepup.domain.dance.api;

import com.google.gson.Gson;
import com.pi.stepup.domain.dance.constant.DanceType;
import com.pi.stepup.domain.dance.constant.ProgressType;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceCreateRequestDto;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceSearchRequestDto;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceUpdateRequestDto;
import com.pi.stepup.domain.dance.dto.DanceResponseDto.DanceFindResponseDto;
import com.pi.stepup.domain.dance.dto.DanceResponseDto.DanceSearchResponseDto;
import com.pi.stepup.domain.dance.service.DanceRedisService;
import com.pi.stepup.domain.dance.service.DanceService;
import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.music.domain.MusicAnswer;
import com.pi.stepup.domain.music.dto.MusicResponseDto.MusicFindResponseDto;
import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.domain.user.service.UserRedisService;
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

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.pi.stepup.domain.dance.constant.DanceResponseMessage.*;
import static com.pi.stepup.domain.dance.constant.DanceUrl.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DanceApiController.class)
public class DanceApiTest {

    @Autowired
    private MockMvc mockMvc;

    private Gson gson;

    @MockBean
    private DanceService danceService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private UserRedisService userRedisService;

    @MockBean
    private DanceRedisService danceRedisService;

    private User host;
    private Music music;
    private Music music2;
    private MusicAnswer musicAnswer;
    private MusicAnswer musicAnswer2;
    private RandomDance randomDance;
    private DanceCreateRequestDto danceCreateRequestDto;
    private DanceUpdateRequestDto danceUpdateRequestDto;
    private DanceSearchRequestDto danceSearchRequestDto;
    private final Long pk = 1L;
    private final Long pk2 = 2L;
    private final String mTitle = "ISTJ";
    private final String artist = "NCT DREAM";
    private final String title = "랜덤 플레이 댄스";
    private final String content = "함께 합시다";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm");
    private final String startAt1 = "2023-07-20 10:00";
    private final String startAt2 = "2023-08-20 10:00";
    private final String endAt = "2023-08-30 10:00";
    private final DanceType type = DanceType.BASIC;
    private List<Long> danceMusicIdList = new ArrayList<>();
    private List<MusicFindResponseDto> allDanceMusic = new ArrayList<>();
    private List<DanceFindResponseDto> allMyDance = new ArrayList<>();
    private List<DanceSearchResponseDto> allDance = new ArrayList<>();

    @BeforeEach
    public void init() {
        gson = new Gson();
        makeHost();
        makeMusic();
        makeMusic2();
        makeDance();

        musicAnswer = MusicAnswer.builder().build();
        musicAnswer2 = MusicAnswer.builder().build();
    }

    public void makeHost() {
        host = User.builder()
                .id("hostId")
                .build();
    }

    public void makeMusic() {
        music = Music.builder()
                .musicId(pk)
                .title(mTitle)
                .artist(artist)
                .build();
        danceMusicIdList.add(pk);
    }

    public void makeMusic2() {
        music2 = Music.builder()
                .musicId(pk2)
                .title(mTitle + "2")
                .artist(artist)
                .build();
        danceMusicIdList.add(pk2);
    }

    public void makeDance() {
        randomDance = RandomDance.builder()
                .randomDanceId(pk)
                .title(title)
                .content(content)
                .host(host)
                .startAt(LocalDateTime.parse(startAt1, formatter))
                .endAt(LocalDateTime.parse(endAt, formatter))
                .build();
    }

    public void makeDanceCreateRequestDto() {
        danceCreateRequestDto
                = DanceCreateRequestDto.builder()
                .title(title)
                .content(content)
                .startAt(startAt1)
                .endAt(endAt)
                .danceType(String.valueOf(type))
                .maxUser(30)
                .hostId(host.getId())
                .danceMusicIdList(danceMusicIdList)
                .build();
    }

    public void makeDanceUpdateRequestDto() {
        danceUpdateRequestDto
                = DanceUpdateRequestDto.builder()
                .randomDanceId(pk)
                .title(title)
                .content(content)
                .startAt(startAt1)
                .endAt(endAt)
                .danceType(String.valueOf(type))
                .maxUser(30)
                .hostId(host.getId())
                .danceMusicIdList(danceMusicIdList)
                .build();
    }

    private void makeAllDanceMusicList() {
        allDanceMusic.add(MusicFindResponseDto.builder().music(music).musicAnswer(musicAnswer).build());
        allDanceMusic.add(MusicFindResponseDto.builder().music(music2).musicAnswer(musicAnswer2).build());
    }

    private void makeAllMyDanceList() {
        allMyDance.add(DanceFindResponseDto.builder().randomDance(randomDance).build());
    }

    public void makeDanceAllSearchRequestDto() {
        danceSearchRequestDto
                = DanceSearchRequestDto.builder()
                .progressType(ProgressType.ALL.toString())
                .keyword("")
                .build();
    }

    @Test
    @DisplayName("랜덤 플레이 댄스 개최 테스트")
    @WithMockUser
    public void createDanceApiTest() throws Exception {
        doNothing().when(danceService).create(any(DanceCreateRequestDto.class));

        makeDanceCreateRequestDto();
        String content = gson.toJson(danceCreateRequestDto);

        mockMvc.perform(
                        post(CREATE_RANDOM_DANCE_URL.getUrl())
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("message")
                        .value(CREATE_RANDOM_DANCE.getMessage()));
    }

    @Test
    @DisplayName("랜덤 플레이 댄스 수정 테스트")
    @WithMockUser
    public void updateDanceApiTest() throws Exception {
        doNothing().when(danceService).update(any(DanceUpdateRequestDto.class));

        makeDanceUpdateRequestDto();
        String content = gson.toJson(danceUpdateRequestDto);

        mockMvc.perform(
                        put(UPDATE_OPEN_RANDOM_DANCE_URL.getUrl())
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("message")
                        .value(UPDATE_OPEN_RANDOM_DANCE.getMessage()));
    }

    @Test
    @DisplayName("랜덤 플레이 댄스 삭제 테스트")
    @WithMockUser
    public void deleteDanceApiTest() throws Exception {
        doNothing().when(danceService).delete(any(Long.class));

        mockMvc.perform(
                        delete(
                                String.format(DELETE_OPEN_RANDOM_DANCE_URL.getUrl()
                                        + "%d", 1))
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("message")
                        .value(DELETE_OPEN_RANDOM_DANCE.getMessage()));
    }

    @Test
    @DisplayName("개최한 랜덤 플레이 댄스 사용 노래 목록 조회 테스트")
    @WithMockUser
    public void readAllDanceMusicApiTest() throws Exception {
        when(danceService.readAllDanceMusic(any(Long.class))).thenReturn(allDanceMusic);

        makeAllDanceMusicList();
        mockMvc.perform(
                        get(
                                String.format(SELECT_ALL_DANCE_MUSIC_URL.getUrl()
                                        + "%d", 1))
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("message")
                        .value(SELECT_ALL_DANCE_MUSIC.getMessage()))
                .andExpect(jsonPath("data").isArray());
    }

    @Test
    @DisplayName("개최한 랜덤 플레이 댄스 목록 조회 테스트")
    @WithMockUser
    public void readAllMyOpenDanceApiTest() throws Exception {
        when(danceService.readAllMyOpenDance()).thenReturn(allMyDance);

        makeAllMyDanceList();
        mockMvc.perform(
                        get(SELECT_ALL_OPEN_RANDOM_DANCE_URL.getUrl())
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("message")
                        .value(SELECT_ALL_OPEN_RANDOM_DANCE.getMessage()))
                .andExpect(jsonPath("data").isArray());
    }

    @Test
    @DisplayName("참여 가능한 랜덤 플레이 댄스 목록 조회 테스트")
    @WithMockUser
    public void readAllDanceApiTest() throws Exception {
        when(danceRedisService.readAllRandomDance(any(DanceSearchRequestDto.class))).thenReturn(allDance);

        makeDanceAllSearchRequestDto();
        mockMvc.perform(
                        get(SELECT_ALL_RANDOM_DANCE_URL.getUrl())
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("message")
                        .value(SELECT_ALL_RANDOM_DANCE.getMessage()))
                .andExpect(jsonPath("data").isArray());
    }

    @Test
    @DisplayName("랜덤 플레이 댄스 예약 테스트")
    @WithMockUser
    public void createReservationApiTest() throws Exception {
        doNothing().when(danceRedisService).createReservation(any(Long.class));

        mockMvc.perform(
                        post(String.format(RESERVE_RANDOM_DANCE_URL.getUrl()
                                + "%d", 1))
                                .with(csrf())
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("message")
                        .value(RESERVE_RANDOM_DANCE.getMessage()));
    }

    @Test
    @DisplayName("랜덤 플레이 댄스 예약 취소 테스트")
    @WithMockUser
    public void deleteReservationApiTest() throws Exception {
        doNothing().when(danceRedisService).deleteReservation(any(Long.class));

        mockMvc.perform(
                        delete(
                                String.format(DELETE_RESERVE_RANDOM_DANCE_URL.getUrl()
                                        + "%d", 1))
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("message")
                        .value(DELETE_RESERVE_RANDOM_DANCE.getMessage()));
    }

    @Test
    @DisplayName("예약한 랜덤 플레이 댄스 목록 조회 테스트")
    @WithMockUser
    public void readAllMyReserveDanceApiTest() throws Exception {
        when(danceRedisService.readAllMyReserveDance()).thenReturn(allMyDance);

        makeAllMyDanceList();
        mockMvc.perform(
                        get(SELECT_ALL_RESERVE_RANDOM_DANCE_URL.getUrl())
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("message")
                        .value(SELECT_ALL_RESERVE_RANDOM_DANCE.getMessage()))
                .andExpect(jsonPath("data").isArray());
    }

    @Test
    @DisplayName("랜덤 플레이 댄스 참여 테스트")
    @WithMockUser
    public void createAttendApiTest() throws Exception {
        doNothing().when(danceService).createAttend(any(Long.class));

        mockMvc.perform(
                        post(String.format(ATTEND_RANDOM_DANCE_URL.getUrl()
                                + "%d", 1))
                                .with(csrf())
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("message")
                        .value(ATTEND_RANDOM_DANCE.getMessage()));
    }

    @Test
    @DisplayName("참여한 랜덤 플레이 댄스 목록 조회 테스트")
    @WithMockUser
    public void readAllMyAttendDanceApiTest() throws Exception {
        when(danceService.readAllMyAttendDance()).thenReturn(allMyDance);

        makeAllMyDanceList();
        mockMvc.perform(
                        get(SELECT_ALL_ATTEND_RANDOM_DANCE_URL.getUrl())
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("message")
                        .value(SELECT_ALL_ATTEND_RANDOM_DANCE.getMessage()))
                .andExpect(jsonPath("data").isArray());
    }
}
