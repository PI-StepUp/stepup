package com.pi.stepup.domain.rank.service;

import com.pi.stepup.domain.rank.dao.PointPolicyRepository;
import com.pi.stepup.domain.rank.domain.PointPolicy;
import com.pi.stepup.domain.rank.dto.RankRequestDto.PointUpdateRequestDto;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointHistoryServiceImpl implements PointHistoryService {
    private final UserRepository userRepository;
    private final PointPolicyRepository pointPolicyRepository;

    @Override
    @Transactional
    public void update(PointUpdateRequestDto pointUpdateRequestDto) {
        User user = userRepository.findById(pointUpdateRequestDto.getId()).orElseThrow();
        PointPolicy pointPolicy = pointPolicyRepository.findOne(pointUpdateRequestDto.getPointPolicyId()).orElseThrow();
        Integer point = pointUpdateRequestDto.getCount() * pointPolicy.getPoint();
        user.updatePoint(point);
    }
}
