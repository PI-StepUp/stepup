package com.pi.stepup.domain.dance.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.pi.stepup.domain.dance.constant.DanceType;
import com.pi.stepup.domain.dance.dao.DanceRepository;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceCreateRequestDto;
import com.pi.stepup.domain.user.domain.User;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class DanceServiceTest {

    @InjectMocks
    private DanceServiceImpl danceService;
    @Mock
    private DanceRepository danceRepository;

    private final String title = "랜덤 플레이 댄스";
    private final String content = "함께 합시다";

    private final String time = "2023-07-25 15:30";
    private final DanceType type = DanceType.BASIC;
    private final int max = 30;
    private final String url = "url";
    private final String id = "id";
    private final String nick = "nick";

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Test
    @DisplayName("랜덤 플레이 댄스 개최 테스트")
    public void createDanceTest() {
        User user = User.builder()
            .id(id)
            .nickname(nick)
            .build();

        DanceCreateRequestDto dancecreateRequestDto
            = DanceCreateRequestDto.builder().title(title)
            .content(content)
            .startAt(time)
            .endAt(time)
            .danceType(type)
            .maxUser(max)
            .thumbnail(url)
//            .host(user)
            .build();

//        RandomDance randomDance = dancecreateRequestDto.toEntity();

//        when(danceRepository.insert(any(RandomDance.class))).thenReturn(randomDance);

        RandomDance createDance = danceService.create(dancecreateRequestDto);

        assertThat(createDance.getTitle()).isEqualTo(title);
        assertThat(createDance.getContent()).isEqualTo(content);

        verify(danceRepository, times(1)).insert(any(RandomDance.class));
    }

}
