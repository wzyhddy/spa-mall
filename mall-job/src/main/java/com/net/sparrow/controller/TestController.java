
package com.net.sparrow.controller;

import com.net.sparrow.service.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job")
public class TestController {

    private static final String SUCCESS = "success";

    @Autowired
    private EmailService emailService;

    @GetMapping("/test")
    public String test() {
        return SUCCESS;
    }

    @GetMapping("/sendEmail")
    public String sendEmail() {
        emailService.sendEmail("wzyhddy@163.com", "test", "test123");
        return SUCCESS;
    }
}