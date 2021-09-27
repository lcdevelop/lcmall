package com.lcsays.gg.dao.manager;

import com.lcsays.gg.models.manager.WxApp;
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
