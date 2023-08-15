package com.pi.stepup.domain.rank.service;

import static com.pi.stepup.domain.dance.constant.DanceExceptionMessage.DANCE_NOT_FOUND;
import static com.pi.stepup.domain.rank.constant.RankExceptionMessage.POINT_POLICY_NOT_FOUND;
import static com.pi.stepup.domain.rank.constant.RankExceptionMessage.RANK_NOT_FOUND;
import static com.pi.stepup.domain.user.constant.UserExceptionMessage.USER_NOT_FOUND;
import static com.pi.stepup.global.config.security.SecurityUtils.getLoggedInUserId;

import com.pi.stepup.domain.dance.dao.DanceRepository;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.dance.exception.DanceBadRequestException;
import com.pi.stepup.domain.rank.dao.PointHistoryRepository;
import com.pi.stepup.domain.rank.dao.PointPolicyRepository;
import com.pi.stepup.domain.rank.dao.RankRepository;
import com.pi.stepup.domain.rank.domain.PointPolicy;
import com.pi.stepup.domain.rank.domain.Rank;
import com.pi.stepup.domain.rank.dto.RankRequestDto.PointUpdateRequestDto;
import com.pi.stepup.domain.rank.dto.RankResponseDto.PointHistoryFindResponseDto;
import com.pi.stepup.domain.rank.exception.PointPolicyNotFoundException;
import com.pi.stepup.domain.rank.exception.RankNotFoundException;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.domain.user.exception.UserNotFoundException;
import com.pi.stepup.domain.user.service.UserService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointHistoryServiceImpl implements PointHistoryService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PointPolicyRepository pointPolicyRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final DanceRepository danceRepository;
    private final RankRepository rankRepository;
    private final PointRedisService pointRedisService;

    @Override
    @Transactional
    public void update(PointUpdateRequestDto pointUpdateRequestDto) {
        String id = getLoggedInUserId();
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));
        PointPolicy pointPolicy = pointPolicyRepository.findOne(
            pointUpdateRequestDto.getPointPolicyId()).orElseThrow(
            () -> new PointPolicyNotFoundException(POINT_POLICY_NOT_FOUND.getMessage()));

        RandomDance randomDance = null;
        if (pointUpdateRequestDto.getRandomDanceId() != null) {
            randomDance = danceRepository.findOne(
                    pointUpdateRequestDto.getRandomDanceId())
                .orElseThrow(() -> new DanceBadRequestException(DANCE_NOT_FOUND.getMessage()));
        }

        Integer point = pointUpdateRequestDto.getCount() * pointPolicy.getPoint();

//        user.updatePoint(point);
        pointHistoryRepository.insert(
            pointUpdateRequestDto.toEntity(user, pointPolicy, randomDance));

        pointRedisService.updatePoint(id, point);
        updateUserRank(id);
    }

    public void updateUserRank(String id) {
        Integer userPoint = userService.readOne().getPoint();
        Rank rank = rankRepository.findOneByPoint(userPoint)
            .orElseThrow(() -> new RankNotFoundException(RANK_NOT_FOUND.getMessage()));
        pointRedisService.updateRank(id, rank.getName());
    }

    @Override
    public List<PointHistoryFindResponseDto> readAll() {
        String id = getLoggedInUserId();
        return pointHistoryRepository.findAll(id).stream()
            .map(
                pointHistory -> PointHistoryFindResponseDto.builder()
                    .pointHistory(pointHistory)
                    .build()
            )
            .collect(Collectors.toList());
    }

    @Override
    public Integer readPoint() {
        return userService.readOne().getPoint();
    }
}
