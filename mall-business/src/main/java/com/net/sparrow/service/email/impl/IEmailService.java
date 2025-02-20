package com.net.sparrow.service.email.impl;

import javax.mail.MessagingException;
import java.util.List;

public interface IEmailService {


    /**
     * 发送简单文本邮件
     *
     * @param receiveEmail 收件人邮箱
     * @param subject      主题
     * @param content      内容
     */
    void sendEmail(String receiveEmail, String subject, String content);

    /**
     * 发送HTML格式的邮件
     *
     * @param receiveEmail 收件人邮箱
     * @param subject      主题
     * @param htmlContent  内容
     * @throws MessagingException
     */
    void sendHtmlEmail(String receiveEmail, String subject, String htmlContent) throws MessagingException;


    /**
     * 发送包含附件的邮件
     *
     * @param receiveEmail 收件人邮箱
     * @param subject      主题
     * @param emailContent 内容
     * @param filePathList 附件地址
     * @throws MessagingException 异常
     */
    void sendAttachmentsEmail(String receiveEmail, String subject, String emailContent, List<String> filePathList) throws MessagingException;
}