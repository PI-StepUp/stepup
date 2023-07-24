package com.pi.stepup.domain.dance.service;

import com.pi.stepup.domain.dance.constant.DanceType;
import com.pi.stepup.domain.dance.dao.DanceRepository;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceSaveRequestDto;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class DanceServiceTest {

    @InjectMocks
    private DanceServiceImpl danceService;
    @Mock
    private DanceRepository danceRepository;

    private final String title = "랜덤 플레이 댄스";
    private final String content = "함께 합시다";
    private final String userCountry = "Asia/Seoul";
    private final LocalDateTime time = LocalDateTime.now(ZoneId.of(userCountry));
    private final DanceType type = DanceType.BASIC;
    private final int max = 30;
    private final String url = "url";

    @Test
    @DisplayName("랜덤 플레이 댄스 개최 테스트")
    public void createDanceTest() {
        RandomDance randomDance = RandomDance.builder()
            .title(title)
            .content(content)
            .startAt(time)
            .endAt(time)
            .danceType(type)
            .maxUser(max)
            .thumbnail(url)
            .build();

        DanceSaveRequestDto danceSaveRequestDto
            = DanceSaveRequestDto.builder().randomDance(randomDance).build();

        when(danceRepository.insert(any(RandomDance.class))).thenReturn(randomDance);

        RandomDance createDance = danceService.create(danceSaveRequestDto);

        assertThat(createDance.getTitle()).isEqualTo(title);
        assertThat(createDance.getContent()).isEqualTo(content);

        verify(danceRepository, times(1)).insert(any(RandomDance.class));
    }

}
