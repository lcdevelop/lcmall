package com.lcsays.lcmall.api.service.manager.impl;

import com.lcsays.lcmall.api.dao.manager.WxAppDao;
import com.lcsays.lcmall.api.models.manager.WxApp;
import com.lcsays.lcmall.api.service.manager.AppService;
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
