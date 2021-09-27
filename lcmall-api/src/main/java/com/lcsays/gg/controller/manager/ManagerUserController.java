package com.lcsays.gg.controller.manager;

import com.lcsays.gg.enums.ErrorCode;
import com.lcsays.gg.models.result.BaseModel;
import com.lcsays.gg.models.weixin.WxMaUser;
import com.lcsays.gg.service.weixin.UserService;
import com.lcsays.gg.utils.ApiUtils;
import com.lcsays.gg.utils.SessionUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

    @GetMapping("/checkLogined")
    public BaseModel<WxMaUser> checkLogined(HttpSession session) {
        String shortSid = SessionUtils.normalizeSessionId(session);
        WxMaUser wxMaUser = userService.getWxMaUserBySessionKey(shortSid);
        if (null != wxMaUser && null != wxMaUser.getSessionWxApp()) {
            if (wxMaUser.isAdmin()
                || wxMaUser.getSessionWxApp().getAuth().equals(0)
                || wxMaUser.getRole().equals(wxMaUser.getSessionWxApp().getName())) {
                SessionUtils.saveUserToSession(session, wxMaUser);
                wxMaUser.clearSecret();
                return BaseModel.success(wxMaUser);
            } else {
                wxMaUser.setSessionWxApp(null);
                userService.update(wxMaUser);
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    @GetMapping("/currentUser")
    public BaseModel<WxMaUser>  currentUser(HttpServletRequest request) {
        if (request.isRequestedSessionIdValid()) {
            WxMaUser wxMaUser = SessionUtils.getUserFromSession(request.getSession());
            if (null != wxMaUser) {
                wxMaUser.clearSecret();
                return BaseModel.success(wxMaUser);
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
    public BaseModel<String> updateRole(HttpSession session, Long userId, String role) {
        WxMaUser user = SessionUtils.getUserFromSession(session);
        if (null != user) {
            if (!user.isAdmin()) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }

            WxMaUser u = userService.getWxMaUserBySimpleId(userId);
            if (null == u) {
                return BaseModel.error(ErrorCode.NO_RESULT);
            }
            u.setRole(role);
            userService.update(u);
            return BaseModel.success();
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    @GetMapping("/user")
    public BaseModel<List<WxMaUser>> user(HttpSession session,
                                          Integer current,
                                          Integer pageSize,
                                          String nickName) {
        WxMaUser user = SessionUtils.getUserFromSession(session);
        if (null != user) {
            if (!user.isAdmin()) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }
            List<WxMaUser> users = userService.userList(nickName);
            for (WxMaUser u: users) {
                u.clearSecret();
            }
            if (current == null) {
                current = 1;
            }
            if (pageSize == null) {
                pageSize = 1000;
            }
            return BaseModel.success(ApiUtils.pagination(users, current, pageSize), users.size());
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }
}
