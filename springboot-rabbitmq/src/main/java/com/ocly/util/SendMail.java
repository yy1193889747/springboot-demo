package com.ocly.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

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
    @Autowired
    private TemplateEngine templateEngine;

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
            log.info("error:{}", from);
        }


    }

    /**
     * 发送带文件的mail
     *
     * @param subject
     * @param to
     * @param context
     * @param filepath
     */
    public void sendFileMail(String subject, String to, String context, String filepath) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setSubject(subject);
            helper.setTo(to);
            helper.setFrom(from);
            helper.setText(context);

            FileSystemResource resource = new FileSystemResource(new File(filepath));
            String filename = filepath.substring(filepath.lastIndexOf(File.separator));
            helper.addAttachment(filename, resource);

            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送 html mail
     *
     * @param subject
     * @param to
     * @param context
     */
    public void sendHtmlMail(String subject, String to, String context) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            // 创建多部件的mail
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setSubject(subject);
            helper.setTo(to);
            helper.setFrom(from);
            helper.setText(context, true);

            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        }


    }

    /**
     * 发送 模板html mail 带附件 带静态资源
     *
     * @param subject
     * @param to
     * @param name
     */
    public void sendtemplateHtmlMail(String subject, String to, String name) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            // 创建多部件的mail
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setSubject(subject);
            helper.setTo(to);
            helper.setFrom(from);
            String filepath = "C:\\Users\\Administrator\\Desktop\\ocly.png";
            FileSystemResource resource = new FileSystemResource(new File(filepath));

            String filename = filepath.substring(filepath.lastIndexOf(File.separator));
            helper.addAttachment(filename, resource);

            Context context = new Context();
            context.setVariable("name", name);
            String templateHtml = templateEngine.process("emailTemplate", context);

            log.info("来看看模板长啥样 {}",templateHtml);

            helper.setText(templateHtml, true);
//            helper.setText("<html><body><img src=\"cid:ocly\" ></body></html>", true);

            helper.addInline("ocly", resource);
            javaMailSender.send(mimeMessage);
            log.info("模板邮件发送成功");
        } catch (MessagingException e) {
            log.error("模板邮件发送失败");
        }


    }

    /**
     * 发送静态邮件
     *
     * @param subject
     * @param to
     * @param context
     * @param filepath
     * @param fileid
     */

    public void sendstaticMail(String subject, String to, String context, String filepath, String fileid) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            // 创建多部件的mail
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setSubject(subject);
            helper.setTo(to);
            helper.setFrom(from);
            helper.setText(context);

            FileSystemResource resource = new FileSystemResource(new File(filepath));
            helper.addInline(fileid, resource);

            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        }


    }
}
