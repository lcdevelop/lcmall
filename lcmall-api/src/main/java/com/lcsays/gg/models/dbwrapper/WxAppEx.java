package com.lcsays.gg.models.dbwrapper;

import com.lcsays.lcmall.db.model.WxApp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: lichuang
 * @Date: 21-9-30 11:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxAppEx extends WxApp {
    private String qrCodePictureUrl;

    public void copyFrom(WxApp wxApp) {
        this.setId(wxApp.getId());
        this.setName(wxApp.getName());
        this.setAppId(wxApp.getAppId());
        this.setImage(wxApp.getImage());
        this.setAuth(wxApp.getAuth());
        this.setWxaCodeUrl(wxApp.getWxaCodeUrl());
        this.setType(wxApp.getType());
        this.setMchId(wxApp.getMchId());
    }
}
