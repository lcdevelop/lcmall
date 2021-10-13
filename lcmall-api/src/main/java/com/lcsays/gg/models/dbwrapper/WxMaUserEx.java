package com.lcsays.gg.models.dbwrapper;

import com.lcsays.lcmall.db.model.WxApp;
import com.lcsays.lcmall.db.model.WxMaUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: lichuang
 * @Date: 21-10-13 10:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxMaUserEx extends WxMaUser {
    private WxApp sessionWxApp;

    public void copyFrom(WxMaUser wxMaUser) {
        this.setId(wxMaUser.getId());
        this.setWxAppId(wxMaUser.getWxAppId());
        this.setOpenid(wxMaUser.getOpenid());
        this.setUnionid(wxMaUser.getUnionid());
        this.setNickname(wxMaUser.getNickname());
        this.setAvatarUrl(wxMaUser.getAvatarUrl());
        this.setGender(wxMaUser.getGender());
        this.setLanguage(wxMaUser.getLanguage());
        this.setCountry(wxMaUser.getCountry());
        this.setCity(wxMaUser.getCity());
        this.setSessionKey(wxMaUser.getSessionKey());
        this.setSessionWxAppId(wxMaUser.getSessionWxAppId());
        this.setRole(wxMaUser.getRole());
        this.setCreateTime(wxMaUser.getCreateTime());
        this.setUpdateTime(wxMaUser.getUpdateTime());
        this.setPhoneNumber(wxMaUser.getPhoneNumber());
    }
}
