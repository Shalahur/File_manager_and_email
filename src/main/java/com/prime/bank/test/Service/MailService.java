package com.prime.bank.test.Service;

import java.util.Map;

public interface MailService {
    void sendSimpleMessage(String to, String subject, String text);

    void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment, Map<String, Boolean> fileType);
}
