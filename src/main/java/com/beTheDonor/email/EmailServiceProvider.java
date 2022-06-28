package com.beTheDonor.email;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
@Slf4j
public class EmailServiceProvider implements EmailSender {

    private final static Logger LOGGER = LoggerFactory.getLogger(EmailServiceProvider.class);
    private final JavaMailSender javaMailSender;

    @Override
    @Async
    public void send(String user_email, String email) {

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,"utf-8");
            helper.setText(email,true);
            helper.setTo(user_email);
            helper.setSubject("Confirm your email");
            helper.setFrom("dhsoni2510@gmail.com");
            javaMailSender.send(mimeMessage);
        }
        catch (MessagingException e)
        {
            LOGGER.error("failed to send email");
            throw new IllegalStateException("failed to send an email");
        }


    }
}
