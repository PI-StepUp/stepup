package com.pi.stepup.domain.user.constant;

import lombok.Getter;

@Getter
public enum EmailGuideContent {

    FIND_ID_GUIDE(
        "[Step Up] 요청하신 아이디 찾기 결과 안내드립니다.",
        "ID 찾기",
        "ID"
    ),
    FIND_PASSWORD_GUIDE(
        "[Step Up] 임시 비밀번호 발급 결과 안내드립니다.",
        "임시 비밀번호 발급",
        "임시 비밀번호"
    );

    private final String mailTitle;
    private final String mailType;
    private final String dataTitle;

    EmailGuideContent(String mailTitle, String mailType, String dataTitle) {
        this.mailTitle = mailTitle;
        this.mailType = mailType;
        this.dataTitle = dataTitle;
    }

}
