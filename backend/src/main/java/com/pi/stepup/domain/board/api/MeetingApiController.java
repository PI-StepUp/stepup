package com.pi.stepup.domain.board.api;

import com.pi.stepup.domain.board.constant.BoardResponseMessage;
import com.pi.stepup.domain.board.dto.meeting.MeetingRequestDto.MeetingSaveRequestDto;
import com.pi.stepup.domain.board.dto.meeting.MeetingRequestDto.MeetingUpdateRequestDto;
import com.pi.stepup.domain.board.service.meeting.MeetingService;
import com.pi.stepup.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/board")
@RestController
public class MeetingApiController {

    private final MeetingService meetingService;

    @PostMapping("/meeting")
    public ResponseEntity<ResponseDto<?>> createMeeting(@RequestBody MeetingSaveRequestDto meetingSaveRequestDto) {

        meetingService.create(meetingSaveRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.create(
                BoardResponseMessage.CREATE_MEETING.getMessage()
        ));
    }


    @PutMapping("/meeting")
    public ResponseEntity<ResponseDto<?>> updateMeeting(@RequestBody MeetingUpdateRequestDto meetingUpdateRequestDto) {

        meetingService.update(meetingUpdateRequestDto).getBoardId();

        return ResponseEntity.ok().body(ResponseDto.create(
                BoardResponseMessage.UPDATE_MEETING.getMessage()
        ));
    }

    @GetMapping("/meeting/{boardId}")
    public ResponseEntity<ResponseDto<?>> readOneMeeting(@PathVariable Long boardId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseDto.create(
                        BoardResponseMessage.READ_ONE_MEETING.getMessage(),
                        meetingService.readOne(boardId)
                )
        );
    }


    @GetMapping("/meeting")
    public ResponseEntity<ResponseDto<?>> readAllMeeting(@RequestParam(required = false) String keyword) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseDto.create(
                        BoardResponseMessage.READ_ALL_MEETING.getMessage(),
                        meetingService.readAll(keyword)
                )
        );
    }


    @DeleteMapping("meeting/{boardId}")
    public ResponseEntity<ResponseDto<?>> deleteMeeting(@PathVariable("boardId") Long boardId) {
        meetingService.delete(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.create(
                BoardResponseMessage.DELETE_MEETING.getMessage()
        ));
    }
}
