package com.lcsays.lcmall.api.controller.evertabs;

import com.lcsays.lcmall.api.config.logger.UserIdLogConverter;
import com.lcsays.lcmall.api.enums.ErrorCode;
import com.lcsays.lcmall.api.models.evertabs.WorkspaceEx;
import com.lcsays.lcmall.api.models.result.BaseModel;
import com.lcsays.lcmall.api.utils.CookieTokenUtils;
import com.lcsays.lcmall.db.model.WxEvertabsFeedback;
import com.lcsays.lcmall.db.model.WxEvertabsTab;
import com.lcsays.lcmall.db.model.WxEvertabsWorkspace;
import com.lcsays.lcmall.db.model.WxMaUser;
import com.lcsays.lcmall.db.service.WxEverTabsFeedbackService;
import com.lcsays.lcmall.db.service.WxEverTabsWorkspaceService;
import com.lcsays.lcmall.db.service.WxMaUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: lichuang
 * @Date: 2022/2/17 21:03
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/evertabs/feedback")
public class FeedbackController {

    @Resource
    WxEverTabsFeedbackService feedbackService;

    @Resource
    WxMaUserService wxMaUserService;

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

    @PostMapping("/addFeedback")
    public BaseModel<String> addFeedback(HttpServletRequest request,
                                               @RequestBody WxEvertabsFeedback feedback) {
        WxMaUser wxMaUser = check(request);
        if (null == wxMaUser) {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }

        feedback.setWxMaUserId(wxMaUser.getId());

        if (feedbackService.addFeedback(feedback)) {
            log.info("addFeedback success {}", feedback);
            return BaseModel.success();
        } else {
            log.error("addFeedback fail {}", feedback);
            return BaseModel.error(ErrorCode.DAO_ERROR);
        }
    }
}
