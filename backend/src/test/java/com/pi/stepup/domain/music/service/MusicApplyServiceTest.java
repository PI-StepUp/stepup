package com.pi.stepup.domain.music.service;

import static com.pi.stepup.domain.music.constant.MusicExceptionMessage.MUSIC_APPLY_NOT_FOUND;
import static com.pi.stepup.domain.music.constant.MusicExceptionMessage.UNAUTHORIZED_USER_ACCESS;
import static com.pi.stepup.domain.user.constant.UserExceptionMessage.USER_NOT_FOUND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pi.stepup.domain.music.dao.MusicApplyRepository;
import com.pi.stepup.domain.music.domain.Heart;
import com.pi.stepup.domain.music.domain.MusicApply;
import com.pi.stepup.domain.music.dto.MusicRequestDto.HeartSaveRequestDto;
import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicApplySaveRequestDto;
import com.pi.stepup.domain.music.dto.MusicResponseDto.MusicApplyFindResponseDto;
import com.pi.stepup.domain.music.exception.MusicApplyNotFoundException;
import com.pi.stepup.domain.music.exception.UnauthorizedUserAccessException;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.domain.user.exception.UserNotFoundException;
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

@ExtendWith(MockitoExtension.class)
class MusicApplyServiceTest {

    @InjectMocks
    MusicApplyServiceImpl musicApplyService;

    @Mock
    MusicApplyRepository musicApplyRepository;

    @Mock
    UserRepository userRepository;

    private MusicApplySaveRequestDto musicApplySaveRequestDto;
    private HeartSaveRequestDto heartSaveRequestDto;
    private MusicApply musicApply;
    private User user;
    private Heart heart;

    @Test
    @BeforeEach
    public void init() {
        makeMusicApplySaveRequestDto();
        makeUser();
        makeMusicApply();
        makeHeartSaveRequestDto();
        makeHeart();
    }

    @Test
    @DisplayName("노래 신청 등록 테스트")
    public void createMusicApplyServiceTest() {
        when(musicApplyRepository.insert(any(MusicApply.class))).thenReturn(musicApply);
        when(userRepository.findById(any(String.class))).thenReturn(Optional.of(user));

        musicApplyService.create(musicApplySaveRequestDto);
        verify(musicApplyRepository, only()).insert(any(MusicApply.class));
    }

    @Test
    @DisplayName("노래 신청 등록 유저 정보 없는 예외 테스트")
    public void noUserCreateMusicApplyServiceTest() {
        when(userRepository.findById(any(String.class))).thenReturn(Optional.empty());

        assertThatThrownBy(
            () -> musicApplyService.create(musicApplySaveRequestDto)
        )
            .isInstanceOf(UserNotFoundException.class)
            .hasMessageContaining(USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("노래 신청 목록 조회 테스트")
    public void readAllMusicApplyServiceTest() {
        String keyword = "";
        List<MusicApply> makedMusicApply = makeMusicApplies();
        doReturn(makedMusicApply)
            .when(musicApplyRepository).findAll(keyword, user.getId());

        List<MusicApplyFindResponseDto> musicApplies = musicApplyService.readAllByKeyword(
            keyword);
        assertThat(musicApplies.size()).isEqualTo(makedMusicApply.size());
    }

    @Test
    @DisplayName("노래 신청 목록 키워드 조회 테스트")
    public void readAllMusicApplyByKeywordServiceTest() {
        String keyword = "1";
        List<MusicApply> madeMusicApply = makeMusicApplies();
        List<MusicApply> keywordMusicApply = makeMusicApplyByKeyword(madeMusicApply, keyword);

        doReturn(keywordMusicApply)
            .when(musicApplyRepository).findAll(keyword, user.getId());

        List<MusicApplyFindResponseDto> musicApplies = musicApplyService.readAllByKeyword(
            keyword);
        assertThat(musicApplies.size()).isEqualTo(keywordMusicApply.size());
    }

    @Test
    @DisplayName("내 노래 신청 목록 조회 테스트")
    public void readAllMusicApplyByUserServiceTest() {
        List<MusicApply> madeMusicApply = makeMusicApplies();
        List<MusicApply> writerMusicApply = makeMusicApplyByUser(madeMusicApply, user);

        doReturn(writerMusicApply)
            .when(musicApplyRepository).findById(user.getId());

        List<MusicApplyFindResponseDto> musicApplies = musicApplyService.readAllById(
            user.getId());
        assertThat(musicApplies.size()).isEqualTo(writerMusicApply.size());
    }


    @Test
    @DisplayName("노래 신청 상세 조회 테스트")
    public void readOneMusicApplyServiceTest() {
        when(musicApplyRepository.findOne(any())).thenReturn(Optional.of(musicApply));

        MusicApplyFindResponseDto result = musicApplyService.readOne(user.getId(),
            musicApply.getMusicApplyId());

        assertThat(result.getTitle()).isEqualTo(musicApply.getTitle());
    }

    @Test
    @DisplayName("없는 노래 신청 상세 조회 예외 테스트")
    public void readOneNotExistMusicApplyServiceTest() {
        when(musicApplyRepository.findOne(any())).thenReturn(Optional.empty());

        assertThatThrownBy(
            () -> musicApplyService.readOne(user.getId(), musicApply.getMusicApplyId()))
            .isInstanceOf(MusicApplyNotFoundException.class)
            .hasMessageContaining(MUSIC_APPLY_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("노래 신청 삭제 테스트")
    public void deleteMusicApplyServiceTest() {
        Long musicId = 1L;

        musicApplyService.delete(musicId);

        verify(musicApplyRepository, only()).delete(musicId);
    }

    @Test
    @DisplayName("노래 신청한 사용자가 아닌 다른 사용자가 삭제 예외 테스트")
    public void deleteMusicApplyNotAuthorizedUserServiceTest() {
        // TODO : claim 에서 사용자 정보 가져와서 체크
        assertThatThrownBy(() -> musicApplyService.delete(musicApply.getMusicApplyId()))
            .isInstanceOf(UnauthorizedUserAccessException.class)
            .hasMessageContaining(UNAUTHORIZED_USER_ACCESS.getMessage());
    }

    @Test
    @DisplayName("없는 노래 신청 삭제 예외 테스트")
    public void deleteNotExistMusicApplyServiceTest() {
        when(musicApplyRepository.findOne(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> musicApplyService.delete(musicApply.getMusicApplyId()))
            .isInstanceOf(MusicApplyNotFoundException.class)
            .hasMessageContaining(MUSIC_APPLY_NOT_FOUND.getMessage());
    }

    private List<MusicApply> makeMusicApplyByKeyword(List<MusicApply> musicApplies,
        String keyword) {
        List<MusicApply> result = new ArrayList<>();

        for (MusicApply m : musicApplies) {
            if (m.getTitle().contains(keyword) || m.getArtist().contains(keyword)) {
                result.add(m);
            }
        }
        return result;
    }

    private List<MusicApply> makeMusicApplyByUser(List<MusicApply> musicApplies, User user) {
        List<MusicApply> result = new ArrayList<>();

        for (MusicApply m : musicApplies) {
            if (m.getWriter().getId().equals(user.getId())) {
                result.add(m);
            }
        }
        return result;
    }

    @Test
    @DisplayName("노래 신청 좋아요 테스트")
    public void musicApplyHeartServiceTest() {
        when(musicApplyRepository.insert(any(Heart.class))).thenReturn(heart);

        // TODO : heartCnt default value null pointer 예외 해결 할 것 (DB에는 잘 들어감)

        musicApplyService.createHeart(heartSaveRequestDto);
        assertThat(musicApply.getHeartCnt()).isEqualTo(1);
    }

    @Test
    @DisplayName("노래 신청 좋아요 취소 테스트")
    public void musicApplyHeartCancelServiceTest() {
        Long heartId = heart.getHeartId();
        // TODO : user, musicApply 더미 데이터 넣기
        when(musicApplyRepository.findHeart(any(), any())).thenReturn(Optional.of(heart));

        musicApplyService.deleteHeart(user.getId(), musicApply.getMusicApplyId());

        verify(musicApplyRepository, only()).deleteHeart(heartId);
    }

    @Test
    @DisplayName("노래 신청 좋아요 상태 체크 테스트")
    public void musicApplyHeartStatusTest() {
        // TODO : Heart 테이블에 해당하는 user && musicApply 있는지 확인
        when(musicApplyRepository.findHeart(any(), any())).thenReturn(Optional.of(heart));

        Integer canHeart = musicApplyService.findHeartStatus(user.getId(),
            musicApply.getMusicApplyId());

        // TODO : 좋아요 안눌렸으면 1, 눌려있으면 0
        final Integer CAN_HEART = 1, CANNOT_HEART = 0;
        assertThat(canHeart).isEqualTo(CAN_HEART);
    }

    private MusicApply updateMusicApply() {
        musicApply.addHeart();
        return musicApply;
    }

    private void makeHeart() {
        heart = Heart.builder()
            .user(user)
            .musicApply(musicApply)
            .build();
    }

    private void makeHeartSaveRequestDto() {
        heartSaveRequestDto = HeartSaveRequestDto.builder()
            .musicApplyId(musicApply.getMusicApplyId())
            .id(user.getId())
            .build();
    }

    private void makeMusicApply() {
        musicApply = MusicApply.builder()
            .title(musicApplySaveRequestDto.getTitle())
            .artist(musicApplySaveRequestDto.getArtist())
            .content(musicApplySaveRequestDto.getContent())
            .writer(user)
            .build();
    }

    private void makeUser() {
        user = User.builder()
            .id("writer")
            .build();
    }

    private void makeMusicApplySaveRequestDto() {
        musicApplySaveRequestDto = MusicApplySaveRequestDto.builder()
            .artist("artist")
            .title("title")
            .content("content")
            .writerId("writer")
            .build();
    }

    private List<MusicApply> makeMusicApplies() {
        List<MusicApply> musicApplies = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            MusicApply tmp = MusicApply.builder().
                title("title" + i)
                .artist("artist" + (i + 1))
                .writer(user)
                .build();
            musicApplies.add(tmp);
        }

        User tmp = User.builder()
            .id("tmp")
            .password("password")
            .build();
        musicApplies.add(MusicApply.builder().
            title("title")
            .artist("artist")
            .writer(tmp)
            .build());
        return musicApplies;
    }
}