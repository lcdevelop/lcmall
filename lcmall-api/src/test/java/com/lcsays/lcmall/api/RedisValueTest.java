package com.lcsays.lcmall.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.Duration;

/**
 * @Author: lichuang
 * @Date: 2022/4/20 12:54
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisValueTest {

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Test
    public void delKey() {
        redisTemplate.delete("https://github.githubassets.com/favicons/favicon-dark.svg");
    }
}
