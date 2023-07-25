package com.pi.stepup.domain.user.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.springframework.core.io.ClassPathResource;

public class EmailMessageMaker {

    public static String makeEmailMessage(String emailType, String dataTitle, String data) {
        try {
            System.out.println("이메일 변환 완료!!!1111");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ClassPathResource("mail/find-mail.html").getInputStream()));
            System.out.println("이메일 변환 완료!!!22222");
            StringBuilder emailContent = new StringBuilder();
            System.out.println("이메일 변환 완료!!!33333333");
            while (bufferedReader.ready()) {
                emailContent.append(bufferedReader.readLine());
            }



            return emailContent.toString()
                .replace("{emailType}", emailType)
                .replace("{dataTitle}", dataTitle)
                .replace("{data}", data);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("이메일 전송 중 예외 발생");
        }
    }
}
