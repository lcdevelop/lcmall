package com.lcsays.gg.handler;

import com.lcsays.gg.builder.TextBuilder;
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
public class SubscribeHandler extends AbstractHandler {

    @Resource
    WxMaUserDao wxMaUserDao;

    @Resource
    WxAppDao wxAppDao;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage,
                                    Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) throws WxErrorException {

        this.logger.info("新关注用户 OPENID: " + wxMpXmlMessage.getFromUser());

        // 获取微信用户基本信息
        try {
            WxMpUser userInfo = wxMpService.getUserService().userInfo(wxMpXmlMessage.getFromUser());
            if (userInfo != null) {
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
            }
        } catch (WxErrorException e) {
            if (e.getError().getErrorCode() == 48001) {
                this.logger.info("该公众号没有获取用户信息权限！");
            }
        }


        WxMpXmlOutMessage responseResult = null;
        try {
            responseResult = this.handleSpecial(wxMpXmlMessage);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }

        if (responseResult != null) {
            return responseResult;
        }

        try {
            return new TextBuilder().build("感谢关注", wxMpXmlMessage, wxMpService);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 处理特殊请求，比如如果是扫码进来的，可以做相应处理
     */
    private WxMpXmlOutMessage handleSpecial(WxMpXmlMessage wxMpXmlMessage)
        throws Exception {
        //TODO
        return null;
    }

}
