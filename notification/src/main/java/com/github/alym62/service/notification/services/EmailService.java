package com.github.alym62.service.notification.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String from;

    public String sendEmailOfNotification(String destination, String text, String message) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(from);
            simpleMailMessage.setTo(destination);
            simpleMailMessage.setSubject(text);
            simpleMailMessage.setText(message);

            emailSender.send(simpleMailMessage);
            return "Email enviado com sucesso";
        } catch (Exception ex) {
            return "Erro ao tentar enviar email";
        }
    }
}
