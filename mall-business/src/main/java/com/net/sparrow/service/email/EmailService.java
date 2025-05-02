package com.net.sparrow.service.email;

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
import java.util.List;

/**
 * 发送email服务
 */
@Component
public class EmailService implements IEmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * 发送简单文本邮件
     *
     * @param receiveEmail 收件人邮箱
     * @param subject      主题
     * @param content      内容
     */
    @Override
    public void sendEmail(String receiveEmail, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(receiveEmail);
        message.setSubject(subject);
        message.setText(content);
        javaMailSender.send(message);
    }

    /**
     * 发送HTML格式的邮件
     *
     * @param receiveEmail 收件人邮箱
     * @param subject      主题
     * @param htmlContent  内容
     * @throws MessagingException
     */
    @Override
    public void sendHtmlEmail(String receiveEmail, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(fromEmail);
        helper.setTo(receiveEmail);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        javaMailSender.send(message);
    }

    /**
     * 发送包含附件的邮件
     *
     * @param receiveEmail 收件人邮箱
     * @param subject      主题
     * @param emailContent 内容
     * @param filePathList 附件地址
     * @throws MessagingException 异常
     */
    @Override
    public void sendAttachmentsEmail(String receiveEmail, String subject, String emailContent, List<String> filePathList) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(fromEmail);
        helper.setTo(receiveEmail);
        helper.setSubject(subject);
        helper.setText(emailContent, true);
        //添加附件资源
        for (String item : filePathList) {
            FileSystemResource file = new FileSystemResource(new File(item));
            String fileName = item.substring(item.lastIndexOf(File.separator));
            helper.addAttachment(fileName, file);
        }
        //发送邮件
        javaMailSender.send(message);
    }
}

