package com.pi.stepup.domain.board.service;

import com.pi.stepup.domain.board.dao.notice.NoticeRepository;
import com.pi.stepup.domain.board.domain.Meeting;
import com.pi.stepup.domain.board.domain.Notice;
import com.pi.stepup.domain.board.dto.meeting.MeetingResponseDto;
import com.pi.stepup.domain.board.dto.notice.NoticeRequestDto;
import com.pi.stepup.domain.board.dto.notice.NoticeResponseDto;
import com.pi.stepup.domain.board.service.notice.NoticeServiceImpl;
import com.pi.stepup.domain.dance.dao.DanceRepository;
import com.pi.stepup.domain.dance.domain.DanceMusic;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.user.constant.UserRole;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.global.config.security.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NoticeServiceTest {
    @InjectMocks
    NoticeServiceImpl noticeService;
    @Mock
    NoticeRepository noticeRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    DanceRepository danceRepository;

    private NoticeRequestDto.NoticeSaveRequestDto noticeSaveRequestDto1;
    private NoticeRequestDto.NoticeSaveRequestDto noticeSaveRequestDto2;
    private NoticeRequestDto.NoticeUpdateRequestDto noticeUpdateRequestDto1;
    private User writer;
    private User adminUser;
    private Notice notice1;
    private Notice notice2;
    private RandomDance randomDance;
    private Music music;
    private DanceMusic danceMusic;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm");

    @Test
    @BeforeEach
    public void init() {
        makeWriter();
        makeAdmin();
        makeMusic();
        makeDance();
        makeDanceMusic();
        makeNotice();
        makeNotice2();
    }

    public User makeWriter() {
        writer = User.builder()
                .id("j3beom")
                .role(UserRole.ROLE_USER)
                .build();
        return writer;
    }

    public User makeAdmin() {
        adminUser = User.builder()
                .id("adminUserId")
                .role(UserRole.ROLE_ADMIN)
                .build();
        return adminUser;
    }

    public void makeNotice() {
        randomDance = makeDance();
        notice1 = Notice.builder()
                .boardId(1L)
                .writer(adminUser)
                .title("공지 테스트 제목")
                .content("공지 테스트 내용")
                .randomDance(randomDance)
                .fileURL("https://example.com/meeting_files/meeting_document.pdf")
                .build();
    }

    public void makeNotice2() {
        randomDance = makeDance();
        notice2 = Notice.builder()
                .boardId(2L)
                .writer(writer)
                .title("공지 테스트 제목2")
                .content("공지 테스트 내용2")
                .randomDance(randomDance)
                .fileURL("https://example.com/meeting_files/meeting_document.pdf")
                .build();
    }

    public RandomDance makeDance() {
        randomDance = RandomDance.builder()
                .randomDanceId(1L)
                .title("랜덤 플레이 댄스")
                .content("함께 합시다")
                .startAt(LocalDateTime.parse("2023-07-20 10:00", formatter))
                .endAt(LocalDateTime.parse("2023-08-30 10:00", formatter))
                .build();
        return randomDance;
    }

    public void makeNoticeSaveRequestDto() {
        noticeSaveRequestDto1 = NoticeRequestDto.NoticeSaveRequestDto.builder()
                .title("공지 테스트 제목")
                .content("공지 테스트 내용")
                .randomDanceId(1L)
                .fileURL("https://example.com/meeting_files/meeting_document.pdf")
                .build();
    }

    public void makeNoticeUpdateRequestDto() {
        noticeUpdateRequestDto1 = NoticeRequestDto.NoticeUpdateRequestDto.builder()
                .boardId(1L)
                .title("(수정)공지 테스트 제목")
                .content("(수정)공지 테스트 내용")
                .randomDanceId(1L)
                .fileURL("(수정)https://example.com/meeting_files/meeting_document.pdf")
                .build();
    }


    public void makeNoticeSaveRequestDto2() {
        noticeSaveRequestDto2 = NoticeRequestDto.NoticeSaveRequestDto.builder()
                .title("공지 테스트 제목2")
                .content("공지 테스트 내용2")
                .randomDanceId(1L)
                .fileURL("https://example.com/meeting_files/meeting_document.pdf")
                .build();
    }

    public Music makeMusic() {
        music = Music.builder()
                .musicId(1L)
                .title("mTitle")
                .artist("artist")
                .build();
        return music;
    }

    public void makeDanceMusic() {
        danceMusic = DanceMusic.createDanceMusic(music);
        randomDance.addDanceMusicAndSetThis(danceMusic);
    }

    @Test
    @DisplayName("공지 게시글 등록 테스트")
    public void createTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMocked = mockStatic(SecurityUtils.class)) {
            securityUtilsMocked.when(SecurityUtils::getLoggedInUserId)
                    .thenReturn(adminUser.getId());

            when(this.userRepository.findById(adminUser.getId()))
                    .thenReturn(Optional.of(adminUser));

            when(danceRepository.findOne(any(Long.class))).thenReturn(Optional.of(randomDance));
            when(noticeRepository.insert(any(Notice.class))).thenReturn(notice1);

            makeNoticeSaveRequestDto();
            Notice savedNotice = noticeService.create(noticeSaveRequestDto1);
            verify(noticeRepository, times(1)).insert(any(Notice.class));

            assertEquals(notice1.getTitle(), savedNotice.getTitle());
        }
    }

    @Test
    @DisplayName("공지 게시글 삭제 테스트")
    public void deleteDanceTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMocked = mockStatic(SecurityUtils.class)) {
            // 관리자 유저로 설정
            securityUtilsMocked.when(SecurityUtils::getLoggedInUserId)
                    .thenReturn(adminUser.getId());
            when(noticeRepository.findOne(any(Long.class))).thenReturn(Optional.of(notice1));
            // 삭제 시도
            noticeService.delete(1L);
            // 삭제 메서드 호출 확인
            verify(noticeRepository, times(1)).delete(1L);
        }
    }

    @Test
    @DisplayName("공지 게시글 수정 테스트")
    public void updateNoticeTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMocked = mockStatic(SecurityUtils.class)) {
            securityUtilsMocked.when(SecurityUtils::getLoggedInUserId)
                    .thenReturn(adminUser.getId());

            // Mock NoticeRepository
            when(noticeRepository.findOne(any(Long.class)))
                    .thenReturn(Optional.of(notice1));
            when(danceRepository.findOne(any(Long.class))).thenReturn(Optional.of(randomDance));
            makeNoticeUpdateRequestDto();

            assertThatNoException().isThrownBy(() -> noticeService.update(noticeUpdateRequestDto1));
        }
    }

    @Test
    @DisplayName("공지 게시글 목록 조회 테스트")
    public void readAllTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMockedStatic = mockStatic(
                SecurityUtils.class)) {
            securityUtilsMockedStatic.when(SecurityUtils::getLoggedInUserId)
                    .thenReturn(adminUser.getId());
            String keyword = "제목";

            List<Notice> makeNotices = new ArrayList<>();
            makeNotices.add(notice1);
            makeNotices.add(notice2);

            when(noticeRepository.findAll(anyString())).thenReturn(makeNotices);

            List<NoticeResponseDto.NoticeInfoResponseDto> result = noticeService.readAll(keyword);

            // 테스트 결과 검증
            assert(result.size() == 2);
            assert(result.get(0).getTitle().equals("공지 테스트 제목"));
            assert(result.get(1).getTitle().equals("공지 테스트 제목2"));
        }
    }
}
