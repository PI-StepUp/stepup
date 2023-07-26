package com.pi.stepup.domain.user.service;

import com.pi.stepup.domain.user.domain.EmailMessage;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendFindIdMail(EmailMessage emailMessage) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            mimeMessage.setSubject(emailMessage.getSubject());
            mimeMessage.addRecipient(RecipientType.TO, new InternetAddress(emailMessage.getTo()));
            mimeMessage.setText(emailMessage.getMessage(), "UTF-8", "html");

            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
}
