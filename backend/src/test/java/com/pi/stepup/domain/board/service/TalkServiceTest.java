package com.pi.stepup.domain.board.service;

import com.pi.stepup.domain.board.dao.talk.TalkRepository;
import com.pi.stepup.domain.board.domain.Talk;
import com.pi.stepup.domain.board.dto.talk.TalkRequestDto.TalkSaveRequestDto;
import com.pi.stepup.domain.board.dto.talk.TalkRequestDto.TalkUpdateRequestDto;
import com.pi.stepup.domain.board.service.talk.TalkServiceImpl;
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

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TalkServiceTest {
    @InjectMocks
    TalkServiceImpl talkService;
    @Mock
    TalkRepository talkRepository;
    @Mock
    UserRepository userRepository;
    private TalkSaveRequestDto talkSaveRequestDto;
    private TalkUpdateRequestDto talkUpdateRequestDto;
    private User writer;
    private Talk talk1;

    @Test
    @BeforeEach
    public void init() {
        makeWriter();
        makeTalk();
        makeTalkSaveRequestDto();
    }

    public User makeWriter() {
        writer = User.builder()
                .id("j3beom")
                .role(UserRole.ROLE_USER)
                .build();
        return writer;
    }


    public void makeTalk() {
        talk1 = Talk.builder()
                .boardId(1L)
                .writer(writer)
                .title("자유게시판 테스트 제목")
                .content("자유게시판 테스트 내용")
                .fileURL("https://example.com/talk_files/meeting_document.pdf")
                .build();
    }


    public void makeTalkSaveRequestDto() {
        talkSaveRequestDto = TalkSaveRequestDto.builder()
                .title("자유게시판 테스트 제목")
                .content("자유게시판 테스트 내용")
                .fileURL("https://example.com/talk_files/meeting_document.pdf")
                .build();
    }

    public void makeTalkUpdateRequestDto() {
        talkUpdateRequestDto = TalkUpdateRequestDto.builder()
                .boardId(1L)
                .title("(수정)자유게시판 테스트 제목")
                .content("(수정)자유게시판 테스트 내용")
                .fileURL("(수정)https://example.com/talk_files/meeting_document.pdf")
                .build();
    }

    @Test
    @DisplayName("자유게시판 게시글 등록 테스트")
    public void createTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMockedStatic = mockStatic(
                SecurityUtils.class)) {
            securityUtilsMockedStatic.when(SecurityUtils::getLoggedInUserId)
                    .thenReturn(writer.getId());
            when(userRepository.findById(any(String.class))).thenReturn(Optional.of(writer));

            makeTalkSaveRequestDto();

            talkService.create(talkSaveRequestDto);
            verify(talkRepository, only()).insert(any(Talk.class));
        }
    }

    @Test
    @DisplayName("자유게시판 게시글 수정 테스트")
    public void updateTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMocked = mockStatic(SecurityUtils.class)) {
            securityUtilsMocked.when(SecurityUtils::getLoggedInUserId)
                    .thenReturn(writer.getId());
            when(talkRepository.findOne(any(Long.class)))
                    .thenReturn(Optional.of(talk1));
            makeTalkUpdateRequestDto();

            assertThatNoException().isThrownBy(() -> talkService.update(talkUpdateRequestDto));
        }
    }

    @Test
    @DisplayName("자유게시판 게시글 삭제 테스트")
    public void deleteTest() {
        try (MockedStatic<SecurityUtils> securityUtilsMockedStatic = mockStatic(SecurityUtils.class)) {
            // Given
            securityUtilsMockedStatic.when(SecurityUtils::getLoggedInUserId).thenReturn(writer.getId());
            when(talkRepository.findOne(any(Long.class))).thenReturn(Optional.of(talk1));
            // When
            talkService.delete(1L);
            // Then
            verify(talkRepository, times(1)).delete(1L);
        }
    }
}
