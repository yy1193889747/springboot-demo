package com.ocly.util;

import com.sun.xml.internal.org.jvnet.mimepull.MIMEMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

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


    public void sendFileMail(String subject, String to,String context, String filepath){

        MimeMessage MimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(MimeMessage, true);
            helper.setSubject(subject);
            helper.setTo(to);
            helper.setFrom(from);
            helper.setText(context);

            FileSystemResource resource = new FileSystemResource(new File(filepath));
            String filename = filepath.substring(filepath.lastIndexOf(File.separator));
            helper.addAttachment();





        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
