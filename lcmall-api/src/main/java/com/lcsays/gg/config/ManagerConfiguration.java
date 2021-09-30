package com.lcsays.gg.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: lichuang
 * @Date: 21-9-1 15:10
 */
@Data
@ConfigurationProperties(prefix = "manager")
public class ManagerConfiguration {
    private String platformAppId;
}
