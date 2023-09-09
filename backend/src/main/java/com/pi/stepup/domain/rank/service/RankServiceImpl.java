package com.pi.stepup.domain.rank.service;

import static com.pi.stepup.domain.rank.constant.RankExceptionMessage.RANK_NOT_FOUND;
import static com.pi.stepup.global.config.security.SecurityUtils.getLoggedInUserId;

import com.pi.stepup.domain.rank.constant.RankName;
import com.pi.stepup.domain.rank.dao.RankRepository;
import com.pi.stepup.domain.rank.domain.Rank;
import com.pi.stepup.domain.rank.dto.RankResponseDto.UserRankFindResponseDto;
import com.pi.stepup.domain.rank.exception.RankNotFoundException;
import com.pi.stepup.domain.user.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RankServiceImpl implements RankService {

    private final UserRepository userRepository;
    private final PointRedisService pointRedisService;
    private final RankRepository rankRepository;

    @Override
    public UserRankFindResponseDto readOne() {
        String id = getLoggedInUserId();

        RankName rankName = pointRedisService.getRankName(id);
        Rank rank = rankRepository.getRankByName(rankName)
            .orElseThrow(() -> new RankNotFoundException(RANK_NOT_FOUND.getMessage()));

        return UserRankFindResponseDto.builder()
            .rank(rank)
            .build();
    }
}
