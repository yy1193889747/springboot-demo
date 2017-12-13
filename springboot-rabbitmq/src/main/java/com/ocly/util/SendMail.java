package com.ocly.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * Created by cy
 * 2017/12/13 19:57
 */
@Component
@Log4j2
public class SendMail {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    /**
     * 发送文本邮件
     *
     * @param subject
     * @param to
     * @param context
     */

    public void sendSimpleMail(String subject, String to, String context) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(context);

        try {
            javaMailSender.send(simpleMailMessage);
            log.info("sucessss:{}", from);
        } catch (Exception e) {
            log.info("error:{}",from);
        }


    }
}
