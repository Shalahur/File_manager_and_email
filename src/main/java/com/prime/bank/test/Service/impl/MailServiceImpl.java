package com.prime.bank.test.Service.impl;

import com.prime.bank.test.Service.MailService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Map;

@Component
public class MailServiceImpl implements MailService {
    private static Logger logger = Logger.getLogger(MailServiceImpl.class);

    @Autowired
    public JavaMailSender emailSender;

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    @Override
    public void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment, Map<String, Boolean> fileType) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            if (fileType.get("isPdf") != null && fileType.get("isPdf")) {
                FileSystemResource file = new FileSystemResource(new File(pathToAttachment + "/Job.pdf"));
                helper.addAttachment("Job.pdf", file);
            }
            if (fileType.get("isCsv") != null && fileType.get("isCsv")) {
                FileSystemResource file = new FileSystemResource(new File(pathToAttachment + "/Job.csv"));
                helper.addAttachment("Job.csv", file);
            }
            if (fileType.get("isExcel") != null && fileType.get("isExcel")) {
                FileSystemResource file = new FileSystemResource(new File(pathToAttachment + "/Job.xlsx"));
                helper.addAttachment("Job.xlsx", file);
            }

            emailSender.send(message);
        } catch (Exception ex) {
            logger.error("Unable to email" + ex);
        }
    }
}
