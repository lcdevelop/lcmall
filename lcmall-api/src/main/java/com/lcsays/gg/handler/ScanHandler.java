package com.lcsays.gg.handler;

import com.lcsays.gg.dao.manager.WxAppDao;
import com.lcsays.gg.dao.weixin.WxMaUserDao;
import com.lcsays.gg.models.manager.WxApp;
import com.lcsays.gg.models.weixin.WxMaUser;
import com.lcsays.gg.utils.SessionUtils;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@Component
public class ScanHandler extends AbstractHandler {

    @Resource
    WxMaUserDao wxMaUserDao;

    @Resource
    WxAppDao wxAppDao;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map,
                                    WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
        WxMpUser userInfo = wxMpService.getUserService().userInfo(wxMpXmlMessage.getFromUser());
        log.info(userInfo.toString());

        String appId = wxMpService.getWxMpConfigStorage().getAppId();
        WxApp wxApp = wxAppDao.getWxAppByAppId(appId);
        if (null == wxApp) {
            return null;
        }

        WxMaUser wxMaUser = wxMaUserDao.getWxMaUserByOpenid(wxApp, userInfo.getOpenId());
        if (null != wxMaUser) {
            SessionUtils.RawSessionIdInfo info = SessionUtils.extractRawSessionIdInfo(wxMpXmlMessage.getEventKey());
            wxMaUser.setSessionKey(info.getSessionId());
            WxApp sessionWxApp = wxAppDao.getWxAppByAppId(info.getSessionAppId());
            wxMaUser.setSessionWxApp(sessionWxApp);
            wxMaUser.parseFrom(userInfo);
            log.info("ready to update wxMaUser: " + wxMaUser);

            wxMaUserDao.update(wxMaUser);
        } else {
            wxMaUser = new WxMaUser(wxApp);
            SessionUtils.RawSessionIdInfo info = SessionUtils.extractRawSessionIdInfo(wxMpXmlMessage.getEventKey());
            wxMaUser.setSessionKey(info.getSessionId());
            WxApp sessionWxApp = wxAppDao.getWxAppByAppId(info.getSessionAppId());
            wxMaUser.setSessionWxApp(sessionWxApp);
            wxMaUser.parseFrom(userInfo);
            log.info("ready to insert wxMaUser: " + wxMaUser);

            if (wxMaUserDao.insert(wxMaUser) <= 0) {
                log.error("wxMaUserDao.insert wxMaUser error: " + wxMaUser);
            }
        }

        return null;
    }
}
