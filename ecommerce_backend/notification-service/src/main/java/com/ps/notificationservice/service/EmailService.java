package com.ps.notificationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    @Value("${SMTP_FROM}")
    private String fromEmail;
    private final JavaMailSender javaMailSender;

    public void sendEmail(String recipient, String subject, String body) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(recipient);
            message.setSubject(subject);
            message.setText(body);
            javaMailSender.send(message);
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
