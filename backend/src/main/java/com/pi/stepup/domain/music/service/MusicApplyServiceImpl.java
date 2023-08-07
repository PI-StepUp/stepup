package com.pi.stepup.domain.music.service;

import static com.pi.stepup.domain.music.constant.MusicExceptionMessage.ADD_HEART_FAIL;
import static com.pi.stepup.domain.music.constant.MusicExceptionMessage.MUSIC_APPLY_DELETE_FAIL;
import static com.pi.stepup.domain.music.constant.MusicExceptionMessage.MUSIC_APPLY_NOT_FOUND;
import static com.pi.stepup.domain.music.constant.MusicExceptionMessage.REMOVE_HEART_FAIL;
import static com.pi.stepup.domain.music.constant.MusicExceptionMessage.UNAUTHORIZED_USER_ACCESS;
import static com.pi.stepup.domain.user.constant.UserExceptionMessage.USER_NOT_FOUND;
import static com.pi.stepup.global.config.security.SecurityUtils.getLoggedInUserId;

import com.pi.stepup.domain.music.dao.MusicApplyRepository;
import com.pi.stepup.domain.music.domain.Heart;
import com.pi.stepup.domain.music.domain.MusicApply;
import com.pi.stepup.domain.music.dto.MusicRequestDto.HeartSaveRequestDto;
import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicApplySaveRequestDto;
import com.pi.stepup.domain.music.dto.MusicResponseDto.MusicApplyFindResponseDto;
import com.pi.stepup.domain.music.exception.HeartStatusException;
import com.pi.stepup.domain.music.exception.MusicApplyNotFoundException;
import com.pi.stepup.domain.music.exception.UnauthorizedUserAccessException;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.domain.user.exception.UserNotFoundException;
import com.pi.stepup.global.error.exception.ForbiddenException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.pi.stepup.global.error.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MusicApplyServiceImpl implements MusicApplyService {

    private final MusicApplyRepository musicApplyRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void create(MusicApplySaveRequestDto musicApplySaveRequestDto) {
        String id = getLoggedInUserId();
        User writer = userRepository.findById(id).orElseThrow(
            () -> new UserNotFoundException(USER_NOT_FOUND.getMessage())
        );

        MusicApply musicApply = musicApplySaveRequestDto.toEntity(writer);
        musicApplyRepository.insert(musicApply);
    }

    @Override
    public List<MusicApplyFindResponseDto> readAllByKeyword(String keyword) {
        String id;
        List<MusicApply> musicApplies;

        try {
            id = getLoggedInUserId();
        } catch (ForbiddenException e) {
            id = null;
        }

        if (id == null) {
            musicApplies = musicApplyRepository.findAll(keyword);
        } else {
            musicApplies = musicApplyRepository.findAll(keyword, id);
            log.info("노래 신청 - 로그인 아이디 : {}", id);
            log.info("신청 목록 : {}", musicApplies);
            if (!musicApplies.get(0).getHearts().isEmpty()) {
                for (Heart h : musicApplies.get(0).getHearts()) {
                    log.info("좋아요 누른사람 아이디 : {}",
                        h.getUser().getId());
                }
            }
        }


        return setCanHeart(musicApplies, id);
    }

    @Override
    public List<MusicApplyFindResponseDto> readAllById() {
        String id = getLoggedInUserId();
        List<MusicApply> musicApplies = musicApplyRepository.findById(id);
        return setCanHeart(musicApplies, id);
    }

    // 원래 메소드
//    public List<MusicApplyFindResponseDto> setCanHeart(List<MusicApply> musicApplies) {
//        List<MusicApplyFindResponseDto> result = new ArrayList<>();
//
//        for (MusicApply ma : musicApplies) {
//            int canHeart = 1;
//            if (ma.getHearts().size() != 0) {
//                canHeart = 0;
//            }
//
//            result.add(MusicApplyFindResponseDto.builder()
//                .musicApply(ma)
//                .canHeart(canHeart)
//                .build());
//        }
//        return result;
//    }

    public List<MusicApplyFindResponseDto> setCanHeart(List<MusicApply> musicApplies, String id) {
        List<MusicApplyFindResponseDto> result = new ArrayList<>();

        for (MusicApply ma : musicApplies) {
            int canHeart = 1;

            for (Heart h : ma.getHearts()) {
                if (h.getUser().getId().equals(id)) {
                    canHeart = 0;
                    break;
                }
            }

            result.add(MusicApplyFindResponseDto.builder()
                .musicApply(ma)
                .canHeart(canHeart)
                .build());
        }
        return result;
    }

    @Override
    public MusicApplyFindResponseDto readOne(Long musicApplyId) {
        return MusicApplyFindResponseDto.builder()
            .musicApply(musicApplyRepository.findOne(musicApplyId)
                .orElseThrow(
                    () -> new MusicApplyNotFoundException(MUSIC_APPLY_NOT_FOUND.getMessage()))
            )
            .canHeart(findHeartStatus(musicApplyId))
            .build();
    }

    @Override
    @Transactional
    public void delete(Long musicApplyId) {
        String id = getLoggedInUserId();

        MusicApply musicApply = musicApplyRepository.findOne(musicApplyId)
            .orElseThrow(
                () -> new MusicApplyNotFoundException(MUSIC_APPLY_DELETE_FAIL.getMessage())
            );

        if (id.equals(musicApply.getWriter().getId())) {
            musicApplyRepository.delete(musicApplyId);
        } else {
            throw new UnauthorizedUserAccessException(UNAUTHORIZED_USER_ACCESS.getMessage());
        }
    }

    @Override
    @Transactional
    public void createHeart(HeartSaveRequestDto heartSaveRequestDto) {
        String id = getLoggedInUserId();
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));
        MusicApply musicApply = musicApplyRepository.findOne(heartSaveRequestDto.getMusicApplyId())
            .orElseThrow(() -> new MusicApplyNotFoundException(MUSIC_APPLY_NOT_FOUND.getMessage()));
        Heart heart = heartSaveRequestDto.toEntity(user, musicApply);

        if (findHeartStatus(musicApply.getMusicApplyId()) == 0) {
            throw new HeartStatusException(ADD_HEART_FAIL.getMessage());
        }

        musicApplyRepository.insert(heart);

        // TODO : Entity 안에 PostPersist, PostRemove
        musicApply.addHeart();
    }

    @Override
    @Transactional
    public void deleteHeart(Long musicRequestId) {
        String id = getLoggedInUserId();
        Optional<Heart> heart = musicApplyRepository.findHeart(id, musicRequestId);

        if (heart.isPresent()) {
            musicApplyRepository.deleteHeart(heart.get().getHeartId());
            MusicApply musicApply = heart.get().getMusicApply();
            musicApply.removeHeart();
        } else {
            throw new HeartStatusException(REMOVE_HEART_FAIL.getMessage());
        }
    }

    @Override
    public Integer findHeartStatus(Long musicApplyId) {
        String id = getLoggedInUserId();
        Optional<Heart> heart = musicApplyRepository.findHeart(id, musicApplyId);

        if (heart.isPresent()) {
            return 0;
        } else {
            return 1;
        }
    }
}
