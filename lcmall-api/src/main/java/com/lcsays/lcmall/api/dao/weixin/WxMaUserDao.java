package com.lcsays.lcmall.api.dao.weixin;

import com.lcsays.lcmall.api.models.manager.WxApp;
import com.lcsays.lcmall.api.models.weixin.WxMaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-8-6 12:31
 */
@Mapper
public interface WxMaUserDao {
    long insert(WxMaUser wxMaUser);
    void update(WxMaUser wxMaUser);
    WxMaUser getWxMaUserByOpenid(@Param("wxApp")WxApp wxApp, @Param("openId") String openId);
    WxMaUser getWxMaUserById(@Param("wxApp")WxApp wxApp, @Param("id") Long id);
    WxMaUser getWxMaUserBySimpleId(@Param("id") Long id);
    WxMaUser getWxMaUserBySessionKey(@Param("sessionKey") String sessionKey);
    List<WxMaUser> userList(String nickName);
}
