package com.lcsays.gg.controller.weixin;

import com.github.binarywang.wxpay.bean.notify.OriginNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyV3Result;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.lcsays.gg.enums.ErrorCode;
import com.lcsays.gg.models.result.BaseModel;
import com.lcsays.gg.models.result.WxResp;
import com.lcsays.gg.utils.SessionUtils;
import com.lcsays.lcmall.db.model.WxApp;
import com.lcsays.lcmall.db.model.WxMaUser;
import com.lcsays.lcmall.db.model.WxMarketingStock;
import com.lcsays.lcmall.db.service.WxAppService;
import com.lcsays.lcmall.db.service.WxMarketingStockService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxCardApiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
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

    @Resource
    WxMarketingStockService wxMarketingStockService;

    @Resource
    WxPayService wxPayService;

    @Resource
    WxAppService wxAppService;

    @Data
    private static class CreateCardParam {
        private String stockId;
    }

    @PostMapping("/createCard")
    public BaseModel<WxCardApiSignature> createCard(HttpSession session,
                                                      @PathVariable String appId,
                                                    @RequestBody CreateCardParam createCardParam
    ) {
        WxMarketingStock wxMarketingStock = wxMarketingStockService.queryByStockId(createCardParam.getStockId());
        String cardId = wxMarketingStock.getCardId();
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

    /**
     * 优惠券消费核销时回调
     * 通过setCallbacks设置此回调地址（https://cloud.lcsays.com/api/wx/mp/{appId}/marketing/payNotify）
     * @param appId
     * @return
     */
    @PostMapping("/payNotify")
    public WxResp payNotify(@PathVariable String appId, @RequestHeader("wechatpay-timestamp") String timestamp,
                            @RequestHeader("wechatpay-nonce") String nonce,
                            @RequestHeader("wechatpay-signature") String signature,
                            @RequestHeader("wechatpay-serial") String serial,
                            @RequestBody String notifyData) {
        log.info(timestamp);
        log.info(nonce);
        log.info(signature);
        log.info(serial);
        log.info("优惠券核销回调： " + notifyData);
        WxApp wxApp = wxAppService.queryByAppId(appId);
        if (null != wxApp) {
            try {
                SignatureHeader header = new SignatureHeader();
                header.setTimeStamp(timestamp);
                header.setNonce(nonce);
                header.setSignature(signature);
                header.setSerial(serial);
                WxPayOrderNotifyV3Result result = wxPayService.switchoverTo(wxApp.getMchId()).parseOrderNotifyV3Result(notifyData, header);
                String outTradeNo = result.getResult().getOutTradeNo();
                String transactionId = result.getResult().getTransactionId();
                String tradeState = result.getResult().getTradeState();
                String successTime = result.getResult().getSuccessTime();

                log.info("======== begin payNotify ");
                log.info(wxApp.getMchId());
                log.info(header.toString());
                log.info(result.toString());
                log.info("======== end payNotify ");
                return WxResp.success();
            } catch (WxPayException e) {
                e.printStackTrace();
                log.error("核销回调处理失败:" + e.getMessage());
                return WxResp.error("核销回调处理失败:" + e.getMessage());
            }

        } else {
            log.error("核销回调处理失败，appId不存在：" + appId);
            return WxResp.error("appId不存在");
        }
    }
}
