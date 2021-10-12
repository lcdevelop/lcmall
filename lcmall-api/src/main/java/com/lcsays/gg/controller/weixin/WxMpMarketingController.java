package com.lcsays.gg.controller.weixin;

import com.google.common.collect.Lists;
import com.lcsays.gg.enums.ErrorCode;
import com.lcsays.gg.models.result.BaseModel;
import com.lcsays.gg.utils.SessionUtils;
import com.lcsays.lcmall.db.model.WxMaUser;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxCardApiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.card.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @Author: lichuang
 * @Date: 21-10-8 12:28
 */
@Slf4j
@RestController
@RequestMapping("/api/wx/mp/{appId}/marketing")
public class WxMpMarketingController {

    @Resource
    WxMpService wxMpService;

    @PostMapping("/createCard")
    public BaseModel<WxCardApiSignature> createCard(HttpSession session,
                                                      @PathVariable String appId
    ) {
        String cardId = "pX2-vjkxkhJzv3AFlRsunjl-1qiE";
        String code = "abc";
        WxMaUser wxMaUser = SessionUtils.getWxUserFromSession(session);
        try {
            String[] param = {cardId, code, wxMaUser.getOpenid()};
            WxCardApiSignature result = wxMpService.switchoverTo(appId).getCardService().createCardApiSignature(param);
            result.setOpenId(wxMaUser.getOpenid());
            result.setCode(code);
            result.setCardId(cardId);
            return BaseModel.success(result);
        } catch (WxErrorException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return BaseModel.errorWithMsg(ErrorCode.WX_SERVICE_ERROR, e.getMessage());
        }
    }
}
