package com.lcsays.gg.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @Author: lichuang
 * @Date: 21-9-1 15:10
 */
@Data
@ConfigurationProperties(prefix = "oss")
public class OSSConfiguration {
    private String endpoint;
    private String keyId;
    private String keySecret;
    private String bucketName;
    private String fileHost;
}
