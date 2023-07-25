package com.pi.stepup.domain.board.api;

import com.pi.stepup.domain.board.service.talk.TalkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class TalkApiController {

    private final TalkService talkService;

}
