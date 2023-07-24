package com.pi.stepup.domain.board.service;

import com.pi.stepup.domain.board.dao.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {

    private MeetingRepository meetingRepository;

}
