package com.lcsays.lcmall.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-9-29 14:17
 */
@Data
@ConfigurationProperties(prefix = "wx.pay")
public class WxPayProperties {

    private List<PayConfig> configs;

    @Data
    public static class PayConfig {
        private String mchId;
        private String mchKey;
        private String appId;
        private String apiV3Key;
        private String privateKeyPath;
        private String privateCertPath;
    }

}
