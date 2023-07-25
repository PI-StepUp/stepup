package com.pi.stepup.domain.user.service;

import com.pi.stepup.domain.user.domain.EmailMessage;

public interface EmailService {

    void sendFindIdMail(EmailMessage emailMessage);
}
