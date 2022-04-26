package com.lcsays.lcmall.api.handler;

import com.lcsays.lcmall.api.builder.TextBuilder;
import com.lcsays.lcmall.api.utils.SessionUtils;
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
public class SubscribeHandler extends AbstractHandler {

    @Resource
    WxAppService wxAppService;

    @Resource
    WxMaUserService wxMaUserService;

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
                com.lcsays.lcmall.db.model.WxApp wxApp = wxAppService.queryByAppId(appId);
                if (null == wxApp) {
                    return null;
                }
// 找自己，找到就更新，找不到就创建
                com.lcsays.lcmall.db.model.WxMaUser wxMaUser =
                        wxMaUserService.getWxMaUserByOpenid(wxApp, userInfo.getOpenId());
                if (null != wxMaUser) {
                    // eventKey是在生成扫描二维码时构造的，形如：wx45fad2175569e690_bad2089f-0185-4744-b923-8e596ca3
                    SessionUtils.RawSessionIdInfo info =
                            SessionUtils.extractRawSessionIdInfo(wxMpXmlMessage.getEventKey());
                    log.info("RawSessionIdInfo: " + info);
                    wxMaUser.setSessionKey(info.getSessionId());
                    com.lcsays.lcmall.db.model.WxApp sessionWxApp = wxAppService.queryByAppId(info.getSessionAppId());
                    wxMaUser.setSessionWxAppId(sessionWxApp.getId());

                    wxMaUser.setOpenid(userInfo.getOpenId());
                    wxMaUser.setUnionid(userInfo.getUnionId());
                    String nickname = userInfo.getNickname()
                            .replaceAll("[\ud800\udc00-\udbff\udfff\ud800-\udfff]", "");
                    if ("".equals(nickname)) {
                        wxMaUser.setNickname("匿名用户");
                    } else {
                        wxMaUser.setNickname(nickname);
                    }
                    if ("".equals(userInfo.getHeadImgUrl())) {
                        wxMaUser.setAvatarUrl("匿名用户");
                    } else {
                        wxMaUser.setAvatarUrl(userInfo.getHeadImgUrl());
                    }
                    wxMaUser.setGender(userInfo.getSexDesc());
                    wxMaUser.setCountry(userInfo.getCountry());
                    wxMaUser.setCity(userInfo.getCity());
                    wxMaUser.setLanguage(userInfo.getLanguage());

                    log.info("ready to update wxMaUser: " + wxMaUser);

                    wxMaUserService.update(wxMaUser);
                } else {
                    wxMaUser = new com.lcsays.lcmall.db.model.WxMaUser();
                    wxMaUser.setWxAppId(wxApp.getId());

                    SessionUtils.RawSessionIdInfo info =
                            SessionUtils.extractRawSessionIdInfo(wxMpXmlMessage.getEventKey());
                    wxMaUser.setSessionKey(info.getSessionId());
                    com.lcsays.lcmall.db.model.WxApp sessionWxApp = wxAppService.queryByAppId(info.getSessionAppId());
                    wxMaUser.setSessionWxAppId(sessionWxApp.getId());
                    wxMaUser.setOpenid(userInfo.getOpenId());
                    wxMaUser.setUnionid(userInfo.getUnionId());
                    String nickname = userInfo.getNickname()
                            .replaceAll("[\ud800\udc00-\udbff\udfff\ud800-\udfff]", "");
                    if ("".equals(nickname)) {
                        wxMaUser.setNickname("匿名用户");
                    } else {
                        wxMaUser.setNickname(nickname);
                    }
                    if ("".equals(userInfo.getHeadImgUrl())) {
                        wxMaUser.setAvatarUrl("匿名用户");
                    } else {
                        wxMaUser.setAvatarUrl(userInfo.getHeadImgUrl());
                    }
                    wxMaUser.setGender(userInfo.getSexDesc());
                    wxMaUser.setCountry(userInfo.getCountry());
                    wxMaUser.setCity(userInfo.getCity());
                    wxMaUser.setLanguage(userInfo.getLanguage());

                    log.info("ready to insert wxMaUser: " + wxMaUser);

                    if (wxMaUserService.insert(wxMaUser) <= 0) {
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
            return new TextBuilder().build("感谢关注，加技术交流群，请加微信warmheartli，并备注evertabs", wxMpXmlMessage, wxMpService);
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
