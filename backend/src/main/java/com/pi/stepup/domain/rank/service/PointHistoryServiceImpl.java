package com.pi.stepup.domain.rank.service;

import com.pi.stepup.domain.dance.dao.DanceRepository;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.rank.constant.RankName;
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
    public void update(PointUpdateRequestDto pointUpdateRequestDto) {
        User user = userRepository.findById(pointUpdateRequestDto.getId()).orElseThrow();
        PointPolicy pointPolicy = pointPolicyRepository.findOne(
            pointUpdateRequestDto.getPointPolicyId()).orElseThrow();
        RandomDance randomDance = danceRepository.findOne(pointUpdateRequestDto.getRandomDanceId());
        Integer point = pointUpdateRequestDto.getCount() * pointPolicy.getPoint();

        user.updatePoint(point);
        pointHistoryRepository.insert(
            pointUpdateRequestDto.toEntity(user, pointPolicy, randomDance));

        // TODO : 유저 포인트 가져오기
        Integer userPoint = user.getPoint();

        // TODO : 포인트 등급 산정 -> JPQL로 getRankName 함수 대체 할 것
//        Rank rank = rankRepository.findOneByPoint(userPoint).orElseThrow();
        RankName rankName = getRankName(userPoint);

        // TODO : 유저 등급 업데이트
        // TODO : 등급 이름으로 등급 가져오기
        Rank rank = rankRepository.getRankByName(rankName).orElseThrow();
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

    private RankName getRankName(Integer point) {
        // 이 부분을 하드코딩하는게 좋을지?
        // 아니면 db에서 rank "start point", "end point" 꺼내와서 계산하는게 좋을지
        if (point >= 0 && point <= 99) {
            return RankName.BRONZE;
        } else if (point >= 100 && point <= 299) {
            return RankName.SILVER;
        } else if (point >= 300 && point <= 499) {
            return RankName.GOLD;
        } else {
            return RankName.PLATINUM;
        }
    }
}
