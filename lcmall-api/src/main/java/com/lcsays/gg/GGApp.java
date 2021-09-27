package com.lcsays.gg;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.lcsays.gg", "com.lcsays.lcmall.db"})
@MapperScan(value = {"com.lcsays.gg.dao", "com.lcsays.lcmall.db.dao"})
public class GGApp {
    public static void main(String[] args) {
        SpringApplication.run(GGApp.class, args);
    }
}
