package com.pi.stepup.domain.user.domain;

import com.pi.stepup.domain.user.constant.EmailGuideContent;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class EmailContent {

    private EmailGuideContent emailGuideContent;
    private String nickname;
    private String data;

}
