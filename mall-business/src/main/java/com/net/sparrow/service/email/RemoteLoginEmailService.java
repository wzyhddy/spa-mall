package com.net.sparrow.service.email;

import com.net.sparrow.entity.email.RemoteLoginEmailEntity;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 异地登录邮件服务
 */
@Service
public class RemoteLoginEmailService {

    @Autowired
    private IEmailService emailService;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;


    /**
     * 发生预警邮件
     *
     * @param emailEntity
     * @throws IOException
     * @throws TemplateException
     * @throws MessagingException
     */
    public void sendEmail(RemoteLoginEmailEntity emailEntity) throws IOException, TemplateException, MessagingException {
        Template template = freeMarkerConfigurer.getConfiguration().getTemplate("remote_login_email.html");
        Map<String, Object> model = new HashMap<>(1);
        model.put("emailEntity", emailEntity);
        String templateHtml = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        emailService.sendHtmlEmail(emailEntity.getEmail(), "账号异地登录提醒", templateHtml);
    }
}
