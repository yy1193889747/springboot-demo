package com.cy.util;

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
     * @param subject 主题
     * @param to 发送方地址
     * @param context 内容
     */

    public void sendSimpleMail(String subject, String to, String context) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();


        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(context);

        try {
            javaMailSender.send(simpleMailMessage);
            log.info("sucess:{}", from);
        } catch (Exception e) {
            log.info("error:{}", from);
        }
    }
}
