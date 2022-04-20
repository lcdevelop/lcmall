package com.lcsays.lcmall.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @Author: lichuang
 * @Date: 2022/4/20 12:54
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MailSenderTest {

    @Resource
    JavaMailSender mailSender;

    @Test
    public void sendMail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("admin@codemeteors.com");
        message.setTo("admin@codemeteors.com");
        message.setSubject("test");
        message.setText("test");
        mailSender.send(message);
    }
}
