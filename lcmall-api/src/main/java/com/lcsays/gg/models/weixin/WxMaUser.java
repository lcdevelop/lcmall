package com.lcsays.gg.models.weixin;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.lcsays.gg.models.manager.WxApp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Author: lichuang
 * @Date: 21-8-5 19:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxMaUser implements Serializable {
    private Long id;

    private WxApp wxApp;
    private String openId;
    private String unionId;
    private String sessionKey;
    private WxApp sessionWxApp;

    private String nickName;
    private String avatarUrl;
    private String gender;
    private String country;
    private String city;
    private String language;
    private String role; // 角色，防止有权限管理的appId，或者admin

    private Timestamp createTime;
    private Timestamp updateTime;

    public WxMaUser(WxApp wxApp) {
        this.wxApp = wxApp;
    }

    public void parseFrom(WxMaJscode2SessionResult session) {
        this.setOpenId(session.getOpenid());
        this.setUnionId(session.getUnionid());
        this.setSessionKey(session.getSessionKey());
    }

    public void parseFrom(WxMaUserInfo userInfo) {
        this.setNickName(userInfo.getNickName());
        this.setAvatarUrl(userInfo.getAvatarUrl());
        this.setGender(userInfo.getGender());
        this.setCountry(userInfo.getCountry());
        this.setCity(userInfo.getCity());
        this.setLanguage(userInfo.getLanguage());
    }

    public void parseFrom(WxMpUser userInfo) {
        this.setOpenId(userInfo.getOpenId());
        this.setUnionId(userInfo.getUnionId());
        this.setNickName(userInfo.getNickname());
        this.setAvatarUrl(userInfo.getHeadImgUrl());
        this.setGender(userInfo.getSexDesc());
        this.setCountry(userInfo.getCountry());
        this.setCity(userInfo.getCity());
        this.setLanguage(userInfo.getLanguage());
    }

    public void clearSecret() {
        this.setOpenId(null);
        this.setUnionId(null);
        this.setSessionKey(null);
    }

    public boolean checkAuthority() {
        return null != role && (
                isAdmin()
                    || sessionWxApp.getAuth().equals(0)
                    || role.equals(sessionWxApp.getAppId()
                )
        );
    }

    public boolean isAdmin() {
        return "admin".equals(role);
    }
}
