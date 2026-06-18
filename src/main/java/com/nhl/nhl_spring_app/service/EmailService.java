package com.nhl.nhl_spring_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String token) {
        String resetUrl = "http://localhost:8081/reset-password?token=" + token;
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Recuperação de Senha - Leafs Nation Shop");
        message.setText("Você solicitou a recuperação de senha.\n\n" +
                        "Clique no link abaixo para criar uma nova senha:\n" +
                        resetUrl + "\n\n" +
                        "Se você não solicitou isso, ignore este e-mail.");
        
        mailSender.send(message);
    }
}
