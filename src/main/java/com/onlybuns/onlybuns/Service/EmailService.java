package com.onlybuns.onlybuns.Service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendActivationEmail(String to, String activationLink) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("Activate your account");
        helper.setText("<p>Click the link below to activate your account:</p><a href=\"" + activationLink + "\">Activate</a>", true);

        mailSender.send(message);
    }

    public void sendInactiveEmail(String mail,String username,long likes) throws MessagingException{
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);

        helper.setTo(mail);
        helper.setSubject("We miss you "  + username + "!");
        helper.setText("<p>It has been a while since you visited our app!</p><p>You have " + likes + " likes on your posts.</p>",true);
        mailSender.send(message);
    }
}
