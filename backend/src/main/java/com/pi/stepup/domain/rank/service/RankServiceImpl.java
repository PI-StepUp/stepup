package com.pi.stepup.domain.rank.service;

import com.pi.stepup.domain.rank.dao.RankRepository;
import com.pi.stepup.domain.rank.dto.RankResponseDto.UserRankFindResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RankServiceImpl implements RankService {
    private final RankRepository rankRepository;

    @Override
    public UserRankFindResponseDto readOne(String id) {
        return UserRankFindResponseDto.builder()
                .rank(rankRepository.findByUserId(id).orElseThrow().getRank())
                .build();
    }
}
