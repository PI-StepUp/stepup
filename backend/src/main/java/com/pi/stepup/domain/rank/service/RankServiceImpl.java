package com.pi.stepup.domain.rank.service;

import com.pi.stepup.domain.rank.dao.RankRepository;
import com.pi.stepup.domain.rank.dto.RankResponseDto.UserRankFindResponseDto;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RankServiceImpl implements RankService {

    private final RankRepository rankRepository;
    private final UserRepository userRepository;

    @Override
    public UserRankFindResponseDto readOne(String id) {
        User user = userRepository.findById(id).orElseThrow();

        return UserRankFindResponseDto.builder()
            .rank(user.getRank())
            .build();
    }
}
