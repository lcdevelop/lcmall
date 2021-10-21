package com.lcsays.lcmall.api.dao.manager;

import com.lcsays.lcmall.api.models.manager.WxApp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-8-11 19:07
 */
@Mapper
public interface WxAppDao {
    List<WxApp> apps();
    WxApp getWxAppByAppId(String appId);
    WxApp getWxAppById(Long id);
}
