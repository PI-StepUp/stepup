package com.pi.stepup.domain.rank.service;

import com.pi.stepup.domain.dance.dao.DanceRepository;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.rank.dao.PointHistoryRepository;
import com.pi.stepup.domain.rank.dao.PointPolicyRepository;
import com.pi.stepup.domain.rank.domain.PointPolicy;
import com.pi.stepup.domain.rank.dto.RankRequestDto.PointUpdateRequestDto;
import com.pi.stepup.domain.rank.dto.RankResponseDto.PointHistoryFindResponseDto;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointHistoryServiceImpl implements PointHistoryService {
    private final UserRepository userRepository;
    private final PointPolicyRepository pointPolicyRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final DanceRepository danceRepository;

    @Override
    @Transactional
    public void update(PointUpdateRequestDto pointUpdateRequestDto) {
        User user = userRepository.findById(pointUpdateRequestDto.getId()).orElseThrow();
        PointPolicy pointPolicy = pointPolicyRepository.findOne(pointUpdateRequestDto.getPointPolicyId()).orElseThrow();
        RandomDance randomDance = danceRepository.findOne(pointUpdateRequestDto.getRandomDanceId());
        Integer point = pointUpdateRequestDto.getCount() * pointPolicy.getPoint();

        user.updatePoint(point);
        pointHistoryRepository.insert(pointUpdateRequestDto.toEntity(user, pointPolicy, randomDance));
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
}
