package com.lcsays.lcmall.api.controller.manager;

import com.lcsays.lcmall.api.enums.ErrorCode;
import com.lcsays.lcmall.api.models.dbwrapper.WxMaUserEx;
import com.lcsays.lcmall.api.models.result.BaseModel;
import com.lcsays.lcmall.api.service.weixin.UserService;
import com.lcsays.lcmall.api.utils.ApiUtils;
import com.lcsays.lcmall.api.utils.SessionUtils;
import com.lcsays.lcmall.db.model.WxApp;
import com.lcsays.lcmall.db.model.WxMaUser;
import com.lcsays.lcmall.db.service.WxAppService;
import com.lcsays.lcmall.db.service.WxMaUserService;
import com.lcsays.lcmall.db.util.WxMaUserUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

    @Resource
    WxMpService wxMpService;

    @Resource
    UserService userService;

    @Resource
    WxMaUserService wxMaUserService;

    @Resource
    WxAppService wxAppService;

    @GetMapping("/refreshSession")
    public BaseModel<String> refreshSession(HttpSession session) {
        session.invalidate();
        return BaseModel.success();
    }

    @GetMapping("/checkLogined")
    public BaseModel<WxMaUser> checkLogined(HttpSession session) {
        String shortSid = SessionUtils.normalizeSessionId(session);
        log.debug("checkLogined shortSid: " + shortSid);
        WxMaUser wxMaUser = wxMaUserService.getWxMaUserBySessionKey(shortSid);
        if (null != wxMaUser && null != WxMaUserUtil.getSessionWxApp(wxMaUser, wxAppService)) {
            if (WxMaUserUtil.checkAuthority(wxMaUser, wxAppService)) {
                SessionUtils.saveWxUserToSession(session, wxMaUser);
                WxMaUserUtil.clearSecret(wxMaUser);
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
