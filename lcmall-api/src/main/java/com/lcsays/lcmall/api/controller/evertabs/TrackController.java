package com.lcsays.lcmall.api.controller.evertabs;

import com.lcsays.lcmall.api.config.logger.UserIdLogConverter;
import com.lcsays.lcmall.api.enums.ErrorCode;
import com.lcsays.lcmall.api.models.result.BaseModel;
import com.lcsays.lcmall.api.utils.CookieTokenUtils;
import com.lcsays.lcmall.api.utils.SessionUtils;
import com.lcsays.lcmall.db.model.WxEvertabsTrack;
import com.lcsays.lcmall.db.model.WxMaUser;
import com.lcsays.lcmall.db.service.WxEverTabsTrackService;
import com.lcsays.lcmall.db.service.WxMaUserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: lichuang
 * @Date: 2022/4/18 08:11
 */
//@Slf4j
@RestController
    @RequestMapping("/api/evertabs/track")
public class TrackController {
    private final Logger trackLogger = LoggerFactory.getLogger("trackLogger");

    @Resource
    WxMaUserService wxMaUserService;

    @Resource
    WxEverTabsTrackService trackService;

    @Data
    private static class DebugParam {
        String msg;
        Object data;
    }

    private WxMaUser check(HttpServletRequest request) {
        String tokenValue = CookieTokenUtils.getTokenValue(request);
        if (null == tokenValue) {
            return null;
        }

        WxMaUser wxMaUser = wxMaUserService.queryUsersByToken(tokenValue);
        if (null != wxMaUser) {
            UserIdLogConverter.LoggerConfigHolder.set(String.valueOf(wxMaUser.getId()));
        }
        return wxMaUser;
    }

    @PostMapping(value = "/debug")
    public String debug(HttpServletRequest request, @RequestBody DebugParam data) {
        check(request);
        trackLogger.info(data.getMsg() + " " + data.getData().toString());
        return "ok";
    }

    @PostMapping(value = "/track")
    public BaseModel<String> track(HttpServletRequest request, @RequestBody String msg) {
        WxMaUser wxMaUser = check(request);
        if (null != wxMaUser) {
            WxEvertabsTrack track = new WxEvertabsTrack();
            track.setWxMaUserId(wxMaUser.getId());
            track.setIp(request.getHeader("x-forwarded-for").split(",")[0]);
            track.setUa(request.getHeader("user-agent"));
            track.setMsg(msg);
            trackService.addTrack(track);
            return BaseModel.success();
        } else {
            return BaseModel.error(ErrorCode.PARAM_ERROR);
        }
    }
}
