package com.pi.stepup.domain.music.service;

import com.pi.stepup.domain.music.dao.MusicApplyRepository;
import com.pi.stepup.domain.music.domain.Heart;
import com.pi.stepup.domain.music.domain.MusicApply;
import com.pi.stepup.domain.music.dto.MusicRequestDto.HeartSaveRequestDto;
import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicApplySaveRequestDto;
import com.pi.stepup.domain.music.dto.MusicResponseDto.MusicApplyFindResponseDto;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MusicApplyServiceImpl implements MusicApplyService {

    private final MusicApplyRepository musicApplyRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public MusicApply create(MusicApplySaveRequestDto musicApplySaveRequestDto) {
        // TODO : user not found exception 예외 처리
        User writer = userRepository.findById(musicApplySaveRequestDto.getWriterId()).orElseThrow();
        MusicApply musicApply = musicApplySaveRequestDto.toEntity(writer);
        return musicApplyRepository.insert(musicApply);
    }

    public List<MusicApplyFindResponseDto> readAllByKeyword(String keyword) {
        return musicApplyRepository.findAll(keyword).stream()
            .map(musicApply -> MusicApplyFindResponseDto.builder()
                .musicApply(musicApply)
                .build())
            .collect(Collectors.toList());
    }

    @Override
    public List<MusicApplyFindResponseDto> readAllById(String id) {
        return musicApplyRepository.findById(id).stream()
            .map(musicApply -> MusicApplyFindResponseDto.builder()
                .musicApply(musicApply)
                .build())
            .collect(Collectors.toList());
    }

    public MusicApplyFindResponseDto readOne(Long musicApplyId) {
        // TODO : not found exception 구현
        return MusicApplyFindResponseDto.builder()
            .musicApply(musicApplyRepository.findOne(musicApplyId).orElseThrow())
            .build();
    }

    @Override
    @Transactional
    public void delete(Long musicId) {
        musicApplyRepository.delete(musicId);
    }

    @Override
    @Transactional
    public void createHeart(HeartSaveRequestDto heartSaveRequestDto) {
        User user = userRepository.findById(heartSaveRequestDto.getId()).orElseThrow();
        MusicApply musicApply = musicApplyRepository.findOne(heartSaveRequestDto.getMusicApplyId())
            .orElseThrow();
        Heart heart = heartSaveRequestDto.toEntity(user, musicApply);

        musicApplyRepository.insert(heart);

        // TODO : Entity 안에 PostPersist, PostRemove
        musicApply.addHeart();
    }

    @Override
    @Transactional
    public void deleteHeart(String id, Long musicRequestId) {
        Heart heart = musicApplyRepository.findHeart(id, musicRequestId);

        musicApplyRepository.deleteHeart(heart.getHeartId());

        MusicApply musicApply = heart.getMusicApply();
        musicApply.removeHeart();
    }
}
