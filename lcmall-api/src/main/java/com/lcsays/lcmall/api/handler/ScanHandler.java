package com.lcsays.lcmall.api.handler;

import com.lcsays.lcmall.api.utils.SessionUtils;
import com.lcsays.lcmall.db.model.WxApp;
import com.lcsays.lcmall.db.model.WxMaUser;
import com.lcsays.lcmall.db.service.WxAppService;
import com.lcsays.lcmall.db.service.WxMaUserService;
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
    WxAppService wxAppService;

    @Resource
    WxMaUserService wxMaUserService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map,
                                    WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
        WxMpUser userInfo = wxMpService.getUserService().userInfo(wxMpXmlMessage.getFromUser());
        log.info(userInfo.toString());

        // 获取系统服务号的wxApp
        String appId = wxMpService.getWxMpConfigStorage().getAppId();
        WxApp wxApp = wxAppService.queryByAppId(appId);
        if (null == wxApp) {
            return null;
        }

        // 找自己，找到就更新，找不到就创建
        WxMaUser wxMaUser = wxMaUserService.getWxMaUserByOpenid(wxApp, userInfo.getOpenId());
        if (null != wxMaUser) {
            // eventKey是在生成扫描二维码时构造的，形如：wx45fad2175569e690_bad2089f-0185-4744-b923-8e596ca3
            SessionUtils.RawSessionIdInfo info = SessionUtils.extractRawSessionIdInfo(wxMpXmlMessage.getEventKey());
            log.info("RawSessionIdInfo: " + info);
            wxMaUser.setSessionKey(info.getSessionId());
            WxApp sessionWxApp = wxAppService.queryByAppId(info.getSessionAppId());
            wxMaUser.setSessionWxAppId(sessionWxApp.getId());

            wxMaUser.setOpenid(userInfo.getOpenId());
            wxMaUser.setUnionid(userInfo.getUnionId());
            String nickname = userInfo.getNickname().replaceAll("[\ud800\udc00-\udbff\udfff\ud800-\udfff]","");
            wxMaUser.setNickname(nickname);
            wxMaUser.setAvatarUrl(userInfo.getHeadImgUrl());
            wxMaUser.setGender(userInfo.getSexDesc());
            wxMaUser.setCountry(userInfo.getCountry());
            wxMaUser.setCity(userInfo.getCity());
            wxMaUser.setLanguage(userInfo.getLanguage());

            log.info("ready to update wxMaUser: " + wxMaUser);

            wxMaUserService.update(wxMaUser);
        } else {
            wxMaUser = new WxMaUser();
            wxMaUser.setWxAppId(wxApp.getId());

            SessionUtils.RawSessionIdInfo info = SessionUtils.extractRawSessionIdInfo(wxMpXmlMessage.getEventKey());
            wxMaUser.setSessionKey(info.getSessionId());
            WxApp sessionWxApp = wxAppService.queryByAppId(info.getSessionAppId());
            wxMaUser.setSessionWxAppId(sessionWxApp.getId());

            wxMaUser.setOpenid(userInfo.getOpenId());
            wxMaUser.setUnionid(userInfo.getUnionId());
            String nickname = userInfo.getNickname().replaceAll("[\ud800\udc00-\udbff\udfff\ud800-\udfff]","");
            wxMaUser.setNickname(nickname);
            wxMaUser.setAvatarUrl(userInfo.getHeadImgUrl());
            wxMaUser.setGender(userInfo.getSexDesc());
            wxMaUser.setCountry(userInfo.getCountry());
            wxMaUser.setCity(userInfo.getCity());
            wxMaUser.setLanguage(userInfo.getLanguage());

            log.info("ready to insert wxMaUser: " + wxMaUser);

            if (wxMaUserService.insert(wxMaUser) <= 0) {
                log.error("wxMaUserDao.insert wxMaUser error: " + wxMaUser);
            }
        }

        return null;
    }
}
