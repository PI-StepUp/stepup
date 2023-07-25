package com.pi.stepup.domain.board.api;

import com.pi.stepup.domain.board.service.meeting.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/board")
@RestController
public class MeetingApiController {

    private final MeetingService meetingService;

}
