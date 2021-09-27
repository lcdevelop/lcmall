package com.lcsays.gg.service.manager;

import com.lcsays.gg.models.manager.WxApp;

import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-9-13 20:55
 */

public interface AppService {
    List<WxApp> apps();
    WxApp getWxAppByAppId(String appId);
    WxApp getWxAppById(Long id);
}
