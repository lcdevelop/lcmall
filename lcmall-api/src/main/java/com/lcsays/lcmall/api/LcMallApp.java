package com.lcsays.lcmall.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.lcsays.lcmall.api", "com.lcsays.lcmall.db"})
@MapperScan(value = {"com.lcsays.lcmall.api.dao", "com.lcsays.lcmall.db.dao"})
@EnableScheduling
public class LcMallApp {
    public static void main(String[] args) {
        SpringApplication.run(LcMallApp.class, args);
    }
}
