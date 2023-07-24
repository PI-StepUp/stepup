package com.pi.stepup.domain.music.service;

import com.pi.stepup.domain.music.dao.MusicApplyRepository;
import com.pi.stepup.domain.music.domain.MusicApply;
import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicApplySaveRequestDto;
import com.pi.stepup.domain.music.dto.MusicResponseDto.MusicApplyFindResponseDto;
import com.pi.stepup.domain.user.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MusicApplyServiceImpl implements MusicApplyService {
    private final MusicApplyRepository musicApplyRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public MusicApply create(MusicApplySaveRequestDto musicApplySaveRequestDto) {
        // TODO : user optional 예외 처리
        MusicApply musicApply = MusicApply.builder()
                .title(musicApplySaveRequestDto.getTitle())
                .artist(musicApplySaveRequestDto.getArtist())
                .content(musicApplySaveRequestDto.getContent())
                .writer(userRepository.findById(musicApplySaveRequestDto.getWriterId()).get())
                .build();

        return musicApplyRepository.insert(musicApply);
    }

    public List<MusicApplyFindResponseDto> readAll(String keyword) {
        return musicApplyRepository.findAll(keyword).stream()
                .map(musicApply -> MusicApplyFindResponseDto.builder()
                        .musicApply(musicApply)
                        .build())
                .collect(Collectors.toList());
    }
}
