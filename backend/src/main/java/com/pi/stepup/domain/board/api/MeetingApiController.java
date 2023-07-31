package com.pi.stepup.domain.board.api;

import com.pi.stepup.domain.board.constant.BoardResponseMessage;
import com.pi.stepup.domain.board.dto.meeting.MeetingRequestDto.MeetingSaveRequestDto;
import com.pi.stepup.domain.board.dto.meeting.MeetingRequestDto.MeetingUpdateRequestDto;
import com.pi.stepup.domain.board.service.meeting.MeetingService;
import com.pi.stepup.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "meeting", description = "meeting domain apis")
@RequiredArgsConstructor
@RequestMapping("/api/board")
@RestController
public class MeetingApiController {

    private final MeetingService meetingService;

    @Operation(summary = "정모 게시글 작성", description = "회원들이 정모 게시글을 작성한다.")
    @ApiResponse(responseCode = "201", description = "정모 등록 완료")
    @PostMapping("/meeting")
    public ResponseEntity<ResponseDto<?>> createMeeting(@RequestBody @Valid MeetingSaveRequestDto meetingSaveRequestDto) {

        meetingService.create(meetingSaveRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.create(
                BoardResponseMessage.CREATE_MEETING.getMessage()
        ));
    }

    @Operation(summary = "정모 게시글 수정",
            description = "내가 작성한 정모 게시글 제목, 내용, 이미지 파일, 시작 시간, 종료 시간, 지역을 수정한다.")
    @ApiResponse(responseCode = "200", description = "정모 수정 완료")
    @PutMapping("/meeting")
    public ResponseEntity<ResponseDto<?>> updateMeeting(@RequestBody @Valid MeetingUpdateRequestDto meetingUpdateRequestDto) {

        meetingService.update(meetingUpdateRequestDto).getBoardId();

        return ResponseEntity.ok().body(ResponseDto.create(
                BoardResponseMessage.UPDATE_MEETING.getMessage()
        ));
    }

    @Operation(summary = "정모 게시글 상세 조회",
            description = "정모 게시글 상세 정보를 조회한다.")
    @ApiResponse(responseCode = "200", description = "정모 게시글 조회 완료")
    @GetMapping("/meeting/{boardId}")
    public ResponseEntity<ResponseDto<?>> readOneMeeting(@PathVariable Long boardId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseDto.create(
                        BoardResponseMessage.READ_ONE_MEETING.getMessage(),
                        meetingService.readOne(boardId)
                )
        );
    }

    @Operation(summary = "정모 게시글 목록 조회", description = "저장되어 있는 모든 정모 게시글의 목록을 조회한다. 검색 기능도 가능하다.")
    @ApiResponse(responseCode = "200",
            description = "정모 목록 조회 완료")
    @GetMapping("/meeting")
    public ResponseEntity<ResponseDto<?>> readAllMeeting(@RequestParam(required = false) String keyword) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseDto.create(
                        BoardResponseMessage.READ_ALL_MEETING.getMessage(),
                        meetingService.readAll(keyword)
                )
        );
    }

    @Operation(summary = "내가 작성한 정모 게시글 목록 조회", description = "내가 작성한 모든 정모 게시글의 목록을 조회한다.")
    @ApiResponse(responseCode = "200",
            description = "내가 작성한 정모 목록 조회")
    @GetMapping("meeting/my")
    public ResponseEntity<ResponseDto<?>> readAllByIdMeeting(Authentication authentication) {

        String id = authentication.getName();
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
                BoardResponseMessage.READ_ALL_MY_MEETING.getMessage(),
                meetingService.readAllById(id)
        ));
    }

    @Operation(summary = "정모 게시글 삭제", description = "작성자만 정모 게시글 삭제가 가능하다.")
    @ApiResponse(responseCode = "200",
            description = "정모 삭제 완료")
    @DeleteMapping("meeting/{boardId}")
    public ResponseEntity<ResponseDto<?>> deleteMeeting(@PathVariable("boardId") Long boardId) {
        meetingService.delete(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
                BoardResponseMessage.DELETE_MEETING.getMessage()
        ));
    }
}
