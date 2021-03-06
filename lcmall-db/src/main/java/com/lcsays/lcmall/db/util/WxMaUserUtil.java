package com.lcsays.lcmall.db.util;

import com.lcsays.lcmall.db.model.WxApp;
import com.lcsays.lcmall.db.model.WxMaUser;
import com.lcsays.lcmall.db.service.WxAppService;

/**
 * @Author: lichuang
 * @Date: 21-10-13 9:49
 */

public class WxMaUserUtil {

    public static boolean isAdmin(WxMaUser wxMaUser) {
        return "admin".equals(wxMaUser.getRole());
    }

    public static WxApp getSessionWxApp(WxMaUser wxMaUser, WxAppService wxAppService) {
        return wxAppService.queryById(wxMaUser.getSessionWxAppId());
    }

    public static boolean checkAuthority(WxMaUser wxMaUser, WxAppService wxAppService) {
        WxApp wxApp = getSessionWxApp(wxMaUser, wxAppService);
        return (
                // 如果是超级公众号，则说明是大家都可以用的2C的应用（比如evertabs），则都有权限，都可登录
                null == wxMaUser.getRole() && wxMaUser.getSessionWxAppId().equals(1)
                )
                || (
                        null != wxMaUser.getRole() && (
                isAdmin(wxMaUser)
                        || wxApp.getAuth().equals(0)
                        || wxMaUser.getRole().equals(wxApp.getAppId()
                )
                        )
        );
    }

    public static void clearSecret(WxMaUser wxMaUser) {
        wxMaUser.setOpenid(null);
        wxMaUser.setUnionid(null);
        wxMaUser.setSessionKey(null);
    }
}
