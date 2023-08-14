package com.pi.stepup.domain.user.util;

import com.pi.stepup.domain.user.domain.EmailContent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.springframework.core.io.ClassPathResource;

public class EmailMessageMaker {

    public static String makeEmailMessage(EmailContent emailContent) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                new ClassPathResource("mail/find-mail.html").getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            while (bufferedReader.ready()) {
                stringBuilder.append(bufferedReader.readLine());
            }

            return stringBuilder.toString()
                .replace("{mailType}", emailContent.getEmailGuideContent().getMailType())
                .replace("{nickname}", emailContent.getNickname())
                .replace("{dataTitle}", emailContent.getEmailGuideContent().getDataTitle())
                .replace("{data}", emailContent.getData());
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("이메일 전송 중 예외 발생");
        }
    }
}
