package com.lcsays.lcmall.api.controller.manager;

import com.lcsays.lcmall.api.config.ManagerConfiguration;
import com.lcsays.lcmall.api.enums.ErrorCode;
import com.lcsays.lcmall.api.models.dbwrapper.WxMaUserEx;
import com.lcsays.lcmall.api.models.result.BaseModel;
import com.lcsays.lcmall.api.service.weixin.UserService;
import com.lcsays.lcmall.api.utils.ApiUtils;
import com.lcsays.lcmall.api.utils.CookieTokenUtils;
import com.lcsays.lcmall.api.utils.SessionUtils;
import com.lcsays.lcmall.db.model.WxApp;
import com.lcsays.lcmall.db.model.WxMaUser;
import com.lcsays.lcmall.db.service.WxAppService;
import com.lcsays.lcmall.db.service.WxMaUserService;
import com.lcsays.lcmall.db.util.WxMaUserUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-8-26 11:23
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/manager/user")
public class ManagerUserController {

    private static final String TOKEN_KEY = "token";

    @Resource
    WxMpService wxMpService;

    @Resource
    UserService userService;

    @Resource
    WxMaUserService wxMaUserService;

    @Resource
    WxAppService wxAppService;

    @Resource
    ManagerConfiguration managerConfiguration;

    @GetMapping("/refreshSession")
    public BaseModel<String> refreshSession(HttpSession session) {
        session.invalidate();
        return BaseModel.success();
    }

    @GetMapping("/checkLogined")
    public BaseModel<WxMaUser> checkLogined(HttpSession session,
                                            HttpServletRequest request,
                                            HttpServletResponse response) {
        // evertabs的登录验证逻辑
        String tokenValue = CookieTokenUtils.getTokenValue(request);
        log.info("tokenValue=" + tokenValue);
        WxMaUser user = wxMaUserService.queryUsersByToken(tokenValue);
        if (null != user) {
            SessionUtils.updateLoggerVarUserId(user);
            return BaseModel.success(user);
        }

        SessionUtils.updateLoggerVarUserId(session);

        String shortSid = SessionUtils.normalizeSessionId(session);
        WxMaUser wxMaUser = wxMaUserService.getWxMaUserBySessionKey(shortSid);
        log.info("checkLogined shortSid: " + shortSid + " wxMaUser: " + wxMaUser);
        if (null != wxMaUser && null != WxMaUserUtil.getSessionWxApp(wxMaUser, wxAppService)) {
            if (WxMaUserUtil.checkAuthority(wxMaUser, wxAppService)) {
                SessionUtils.saveWxUserToSession(session, wxMaUser);
                WxMaUserUtil.clearSecret(wxMaUser);

                String token = CookieTokenUtils.setTokenValue(response);
                wxMaUser.setToken(token);
                log.info(token);
                wxMaUserService.update(wxMaUser);
                return BaseModel.success(wxMaUser);
            } else {
                wxMaUser.setSessionWxAppId(null);
                wxMaUserService.update(wxMaUser);
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    /**
     * 通过token判断的登出
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/logoutT")
    public BaseModel<String> logoutT(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        for (Cookie cookie: request.getCookies()) {
            if (cookie.getName().equals(TOKEN_KEY)) {
                String tokenValue = cookie.getValue();
                WxMaUser wxMaUser = wxMaUserService.queryUsersByToken(tokenValue);
                if (null != wxMaUser) {
                    session.invalidate();
                    CookieTokenUtils.deleteTokenValue(response);
                    return BaseModel.success();
                }
            }
        }
        return BaseModel.error(ErrorCode.NEED_LOGIN);
    }

    @GetMapping("/currentUser")
    public BaseModel<WxMaUserEx>  currentUser(HttpServletRequest request) {
        if (request.isRequestedSessionIdValid()) {
            WxMaUser wxMaUser = SessionUtils.getWxUserFromSession(request.getSession());
            if (null != wxMaUser) {
                WxMaUserUtil.clearSecret(wxMaUser);
                WxMaUserEx wxMaUserEx = new WxMaUserEx();
                wxMaUserEx.copyFrom(wxMaUser);
                WxApp wxApp = wxAppService.queryById(wxMaUser.getSessionWxAppId());
                wxMaUserEx.setSessionWxApp(wxApp);
                return BaseModel.success(wxMaUserEx);
            } else {
                return BaseModel.error(ErrorCode.NEED_LOGIN);
            }
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    /**
     * 通过token判断的当前用户
     * @param request
     * @return
     */
    @GetMapping("/currentUserT")
    public BaseModel<WxMaUserEx>  currentUserT(HttpServletRequest request,
                                               HttpServletResponse response,
                                               String token) {
//        String tokenValue = CookieTokenUtils.getTokenValue(request);
        String tokenValue = token;
        if (null != tokenValue) {
            WxMaUser wxMaUser = wxMaUserService.queryUsersByToken(tokenValue);
            if (null != wxMaUser) {
                WxMaUserEx wxMaUserEx = new WxMaUserEx();
                wxMaUserEx.copyFrom(wxMaUser);
                WxApp wxApp = wxAppService.queryById(wxMaUser.getSessionWxAppId());
                wxMaUserEx.setSessionWxApp(wxApp);
                CookieTokenUtils.setTokenValue(response, token);
                return BaseModel.success(wxMaUserEx);
            }
        }
        return BaseModel.error(ErrorCode.NEED_LOGIN);
    }

    @GetMapping("/loginQrCodePictureUrl")
    public BaseModel<String> loginQrCodePictureUrl(HttpSession session) {
        String shortSid = SessionUtils.normalizeSessionId(session);
        try {
            // 这里形成的sessionId也就是回调的eventKey格式类似：wxfe9faf8c8e3a5830_68822c00-7ca8-4762-9ffc-d1bd455fe49d
            String sceneStr = SessionUtils.addPrefix(shortSid, managerConfiguration.getPlatformAppId());
            WxMpQrCodeTicket ticket = wxMpService.switchoverTo(managerConfiguration.getPlatformAppId())
                    .getQrcodeService().qrCodeCreateTmpTicket(
                            sceneStr,
                            10000
                    );
            String url = wxMpService.switchoverTo(managerConfiguration.getPlatformAppId()).getQrcodeService()
                    .qrCodePictureUrl(ticket.getTicket());
            return BaseModel.success(url);
        } catch (WxErrorException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return BaseModel.error(ErrorCode.WX_SERVICE_ERROR);
        }
    }

    @PostMapping("/outLogin")
    public BaseModel<String> outLogin(HttpSession session) {
        session.invalidate();
        return BaseModel.success("ok");
    }

    @PostMapping("/updateRole")
    public BaseModel<String> updateRole(HttpSession session, Integer userId, String role) {
        WxMaUser user = SessionUtils.getWxUserFromSession(session);
        if (null != user) {
            if (!WxMaUserUtil.isAdmin(user)) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }

            WxMaUser u = wxMaUserService.getWxMaUserById(userId);
            if (null == u) {
                return BaseModel.error(ErrorCode.NO_RESULT);
            }
            u.setRole(role);
            wxMaUserService.update(u);
            // 每当更新了数据库要重新刷到session里
            SessionUtils.updateWxMaUser2Session(wxMaUserService, session, u.getId());
            return BaseModel.success();
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    @GetMapping("/user")
    public BaseModel<List<WxMaUserEx>> user(HttpSession session,
                                          Integer current,
                                          Integer pageSize,
                                          String nickName) {
        WxMaUser user = SessionUtils.getWxUserFromSession(session);
        if (null != user) {
            if (!WxMaUserUtil.isAdmin(user)) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }
            List<WxMaUser> users = null;
            if (null != nickName) {
                users = wxMaUserService.queryUsersByNickName(nickName);
            } else {
                users = wxMaUserService.listUsers();
            }

            List<WxMaUserEx> ret = new ArrayList<>();
            for (WxMaUser u: users) {
                WxMaUserUtil.clearSecret(u);

                WxMaUserEx wxMaUserEx = new WxMaUserEx();
                wxMaUserEx.copyFrom(u);
                if (null != u.getWxAppId()) {
                    WxApp wxApp = wxAppService.queryById(u.getWxAppId());
                    wxMaUserEx.setWxApp(wxApp);
                }
                if (null != u.getSessionWxAppId()) {
                    WxApp wxApp = wxAppService.queryById(u.getSessionWxAppId());
                    wxMaUserEx.setSessionWxApp(wxApp);
                }
                ret.add(wxMaUserEx);
            }
            if (current == null) {
                current = 1;
            }
            if (pageSize == null) {
                pageSize = 1000;
            }
            return BaseModel.success(ApiUtils.pagination(ret, current, pageSize), ret.size());
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }
}
