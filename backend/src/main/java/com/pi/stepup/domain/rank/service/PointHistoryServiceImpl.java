package com.pi.stepup.domain.rank.service;

import com.pi.stepup.domain.dance.dao.DanceRepository;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.rank.dao.PointHistoryRepository;
import com.pi.stepup.domain.rank.dao.PointPolicyRepository;
import com.pi.stepup.domain.rank.dao.RankRepository;
import com.pi.stepup.domain.rank.domain.PointPolicy;
import com.pi.stepup.domain.rank.domain.Rank;
import com.pi.stepup.domain.rank.dto.RankRequestDto.PointUpdateRequestDto;
import com.pi.stepup.domain.rank.dto.RankResponseDto.PointHistoryFindResponseDto;
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
public class PointHistoryServiceImpl implements PointHistoryService {

    private final UserRepository userRepository;
    private final PointPolicyRepository pointPolicyRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final DanceRepository danceRepository;
    private final RankRepository rankRepository;

    @Override
    @Transactional
    /**
     * TODO : 리팩토링
     *  - 포인트 업데이트, 랭크 업데이트 메소드 두개로 분리
     */
    public void update(PointUpdateRequestDto pointUpdateRequestDto) {
        User user = userRepository.findById(pointUpdateRequestDto.getId()).orElseThrow();
        PointPolicy pointPolicy = pointPolicyRepository.findOne(
            pointUpdateRequestDto.getPointPolicyId()).orElseThrow();
        RandomDance randomDance = danceRepository.findOne(pointUpdateRequestDto.getRandomDanceId())
            .orElseThrow();
        Integer point = pointUpdateRequestDto.getCount() * pointPolicy.getPoint();

        user.updatePoint(point);
        pointHistoryRepository.insert(
            pointUpdateRequestDto.toEntity(user, pointPolicy, randomDance));

        Integer userPoint = user.getPoint();
        Rank rank = rankRepository.findOneByPoint(userPoint).orElseThrow();
        user.setRank(rank);
    }

    @Override
    public List<PointHistoryFindResponseDto> readAll() {
        return pointHistoryRepository.findAll().stream()
            .map(
                pointHistory -> PointHistoryFindResponseDto.builder()
                    .pointHistory(pointHistory)
                    .build()
            )
            .collect(Collectors.toList());
    }

    @Override
    public Integer readPoint(String id) {
        User user = userRepository.findById(id).orElseThrow();
        return user.getPoint();
    }
}
