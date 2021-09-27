package com.lcsays.gg.service.manager.impl;

import com.lcsays.gg.dao.manager.WxAppDao;
import com.lcsays.gg.models.manager.WxApp;
import com.lcsays.gg.service.manager.AppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-9-13 20:55
 */
@Service
@Slf4j
public class AppServiceImpl implements AppService {

    @Resource
    WxAppDao wxAppDao;

    @Override
    public List<WxApp> apps() {
        return wxAppDao.apps();
    }

    @Override
    public WxApp getWxAppByAppId(String appId) {
        return wxAppDao.getWxAppByAppId(appId);
    }

    @Override
    public WxApp getWxAppById(Long id) {
        return wxAppDao.getWxAppById(id);
    }
}
