package com.haier.adp.sla.controller;

/**
 * Created by Administrator on 2017/1/3.
 */
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("mail")
public class MailController {
    @Autowired
    JavaMailSender mailSender;

    @RequestMapping("sendemail")
    public Object sendEmail()
    {
        try
        {
            final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setFrom("zhang_ap@penghaisoft.com");
            message.setTo("liyh@penghaisoft.com");
            message.setSubject("测试邮件主题");
            message.setText("测试邮件内容");
            this.mailSender.send(mimeMessage);

            return null;
        }
        catch(Exception ex)
        {
            return null;
        }
    }
}