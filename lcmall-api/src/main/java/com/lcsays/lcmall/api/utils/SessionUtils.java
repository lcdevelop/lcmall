package com.lcsays.lcmall.api.utils;

import com.lcsays.lcmall.api.models.weixin.WxMaUser;
import com.lcsays.lcmall.db.service.WxMaUserService;
import lombok.Data;

import javax.servlet.http.HttpSession;

/**
 * @Author: lichuang
 * @Date: 21-8-30 10:39
 */

public class SessionUtils {
    private static final String USER_SESSION_KEY = "user";
    private static final String WX_USER_SESSION_KEY = "wx_user";

    @Data
    public static class RawSessionIdInfo {
        private String wxPrefix;
        private String sessionAppId;
        private String sessionId;
    }

    private static String shortenSessionId(String sessionId) {
        int len = sessionId.length();
        return sessionId.substring(0, Math.min(len, 32));
    }

    /**
     * 从session正规化到字符串，作为key，用于标识某个登录用户
     * @param session
     * @return
     */
    public static String normalizeSessionId(HttpSession session) {
        String sessionId = session.getId();
        sessionId = sessionId.replace('_', '-'); // 去掉所有_，之后可以作为特定分隔符
        return shortenSessionId(sessionId);
    }


    /**
     * 以_分隔的session，则取出最后一部分（前面部分是特殊标识，比如qrscene_）
     * @param rawSessionId
     * @return
     */
    public static String normalizeSessionId(String rawSessionId) {
        String[] array = rawSessionId.split("_");
        if (array.length > 1) {
            return shortenSessionId(array[array.length-1]);
        } else {
            return shortenSessionId(array[0]);
        }
    }

    public static RawSessionIdInfo extractRawSessionIdInfo(String rawSessionId) {
        RawSessionIdInfo rawSessionIdInfo = new RawSessionIdInfo();
        String[] array = rawSessionId.split("_");
        if (array.length == 3) {
            rawSessionIdInfo.setWxPrefix(array[0]);
            rawSessionIdInfo.setSessionAppId(array[1]);
            rawSessionIdInfo.setSessionId(array[2]);
        } else if (array.length == 2) {
            if (array[0].indexOf("wx") == 0) {
                rawSessionIdInfo.setSessionAppId(array[0]);
            } else {
                rawSessionIdInfo.setWxPrefix(array[0]);
            }
            rawSessionIdInfo.setSessionId(array[1]);
        } else if (array.length == 1) {
            rawSessionIdInfo.setSessionId(array[0]);
        }
        return rawSessionIdInfo;
    }

    public static String addPrefix(String sessionId, String prefix) {
        return prefix + "_" + sessionId;
    }

    public static void saveUserToSession(HttpSession session, WxMaUser wxMaUser) {
        session.setAttribute(USER_SESSION_KEY, wxMaUser);
    }

    public static WxMaUser getUserFromSession(HttpSession session) {
        return (WxMaUser)session.getAttribute(USER_SESSION_KEY);
    }

    public static void saveWxUserToSession(HttpSession session, com.lcsays.lcmall.db.model.WxMaUser wxMaUser) {
        session.setAttribute(WX_USER_SESSION_KEY, wxMaUser);
    }

    public static com.lcsays.lcmall.db.model.WxMaUser getWxUserFromSession(HttpSession session) {
        return (com.lcsays.lcmall.db.model.WxMaUser)session.getAttribute(WX_USER_SESSION_KEY);
    }

    public static void updateWxMaUser2Session(WxMaUserService service, HttpSession session, Integer wxMaUserId) {
        com.lcsays.lcmall.db.model.WxMaUser wxMaUser = service.getWxMaUserById(wxMaUserId);
        saveWxUserToSession(session, wxMaUser);
    }
}
