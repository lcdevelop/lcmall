package com.lcsays.lcmall.api.controller.weixin;

import com.github.binarywang.wxpay.bean.ecommerce.SignatureHeader;
import com.github.binarywang.wxpay.bean.marketing.FavorCouponsUseResult;
import com.github.binarywang.wxpay.bean.marketing.UseNotifyData;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.lcsays.lcmall.api.enums.ErrorCode;
import com.lcsays.lcmall.api.models.result.BaseModel;
import com.lcsays.lcmall.api.models.result.WxResp;
import com.lcsays.lcmall.api.utils.SessionUtils;
import com.lcsays.lcmall.api.utils.TimeUtils;
import com.lcsays.lcmall.db.model.*;
import com.lcsays.lcmall.db.service.WxAppService;
import com.lcsays.lcmall.db.service.WxMarketingActivityService;
import com.lcsays.lcmall.db.service.WxMarketingCouponService;
import com.lcsays.lcmall.db.service.WxMarketingStockService;
import com.lcsays.lcmall.db.util.WxMarketingActivityUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxCardApiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

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

    @Resource
    WxMarketingCouponService wxMarketingCouponService;

    @Resource
    WxMarketingActivityService activityService;

    @Data
    private static class CreateCardParam {
        private Integer activityId;
    }

    @PostMapping("/createCard")
    public BaseModel<List<WxCardApiSignature>> createCard(HttpSession session,
                                                         @PathVariable String appId,
                                                         @RequestBody CreateCardParam createCardParam
    ) {
        WxMarketingActivity activity = activityService.queryById(createCardParam.getActivityId());
        if (null == activity) {
            return BaseModel.error(ErrorCode.PARAM_ERROR);
        }

        WxMaUser wxMaUser = SessionUtils.getWxUserFromSession(session);

        List<WxCardApiSignature> data = new ArrayList<>();

        for (String stockId: WxMarketingActivityUtil.getStockIds(activity)) {
            WxMarketingStock wxMarketingStock = wxMarketingStockService.queryByStockId(stockId);
            String cardId = wxMarketingStock.getCardId();
            String code = "abc";

            try {
                String[] param = {cardId, code, wxMaUser.getOpenid()};
                WxCardApiSignature result = wxMpService.switchoverTo(appId).getCardService().createCardApiSignature(param);
                result.setOpenId(wxMaUser.getOpenid());
                result.setCode(code);
                result.setCardId(cardId);
                data.add(result);
            } catch (WxErrorException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                return BaseModel.errorWithMsg(ErrorCode.WX_SERVICE_ERROR, e.getMessage());
            }
        }

        return BaseModel.success(data);
    }

    /**
     * 优惠券消费核销时回调
     * 通过setCallbacks设置此回调地址（https://cloud.lcsays.com/api/wx/mp/{appId}/marketing/payNotify）
     * @param appId
     * @return
     */
    @PostMapping("/payNotify")
    public WxResp payNotify(@PathVariable String appId,
                            @RequestHeader("wechatpay-timestamp") String timestamp,
                            @RequestHeader("wechatpay-nonce") String nonce,
                            @RequestHeader("wechatpay-signature") String signature,
                            @RequestHeader("wechatpay-serial") String serial,
                            @RequestBody String notifyData) {

        WxApp wxApp = wxAppService.queryByAppId(appId);
        if (null != wxApp) {
            try {
                SignatureHeader header = new SignatureHeader();
                header.setTimeStamp(timestamp);
                header.setNonce(nonce);
                header.setSigned(signature);
                header.setSerialNo(serial);
                UseNotifyData data = wxPayService.switchoverTo(wxApp.getMchId())
                        .getMarketingFavorService().parseNotifyData(notifyData, header);
                FavorCouponsUseResult result = wxPayService.switchoverTo(wxApp.getMchId())
                        .getMarketingFavorService().decryptNotifyDataResource(data);

                log.info("优惠券核销事件回调: " + result);

                String stockId = result.getStockId();
                String couponId = result.getCouponId();
                String status = result.getStatus();
                String createTime = result.getCreateTime();
                String consumeTime = result.getConsumeInformation().getConsumeTime();
                String consumeMchid = result.getConsumeInformation().getConsumeMchid();
                String transactionId = result.getConsumeInformation().getTransactionId();

                WxMarketingCoupon wxMarketingCoupon = new WxMarketingCoupon();
                wxMarketingCoupon.setStockId(stockId);
                wxMarketingCoupon.setCouponId(couponId);
                wxMarketingCoupon.setStatus(status);
                wxMarketingCoupon.setCreateTime(TimeUtils.rfc33992Date(createTime));
                wxMarketingCoupon.setConsumeTime(TimeUtils.rfc33992Date(consumeTime));
                wxMarketingCoupon.setConsumeMchid(consumeMchid);
                wxMarketingCoupon.setTransactionId(transactionId);
                if (wxMarketingCouponService.update(wxMarketingCoupon) > 0) {
                    return WxResp.success();
                } else {
                    log.error("payNotify更新数据库出错: " + result);
                    return WxResp.error("更新数据库出错");
                }
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
