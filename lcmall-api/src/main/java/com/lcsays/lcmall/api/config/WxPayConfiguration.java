package com.lcsays.lcmall.api.config;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: lichuang
 * @Date: 21-9-29 14:17
 */
@AllArgsConstructor
@Configuration
@EnableConfigurationProperties(WxPayProperties.class)
public class WxPayConfiguration {

    private final WxPayProperties properties;

    @Bean
    public WxPayService wxPayService() {
        WxPayService service = new WxPayServiceImpl();
        Map<String, WxPayConfig> m = new HashMap<>();
        for (WxPayProperties.PayConfig config : properties.getConfigs()) {
            WxPayConfig conf = new WxPayConfig();
            conf.setMchId(config.getMchId());
            conf.setMchKey(config.getMchKey());
            conf.setAppId(config.getAppId());
            conf.setApiV3Key(config.getApiV3Key());
            conf.setPrivateKeyPath(config.getPrivateKeyPath());
            conf.setPrivateCertPath(config.getPrivateCertPath());
            m.put(config.getMchId(), conf);
        }
        service.setMultiConfig(m);
        return service;
    }
}
