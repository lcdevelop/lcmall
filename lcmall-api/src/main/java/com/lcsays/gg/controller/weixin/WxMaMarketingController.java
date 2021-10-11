package com.lcsays.gg.controller.weixin;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.urllink.GenerateUrlLinkRequest;
import com.github.binarywang.wxpay.bean.marketing.FavorCouponsCreateRequest;
import com.github.binarywang.wxpay.bean.marketing.FavorCouponsCreateResult;
import com.github.binarywang.wxpay.bean.marketing.FavorCouponsGetResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.google.common.collect.Lists;
import com.lcsays.gg.enums.ErrorCode;
import com.lcsays.gg.models.result.BaseModel;
import com.lcsays.gg.utils.RequestNo;
import com.lcsays.gg.utils.SessionUtils;
import com.lcsays.lcmall.db.model.WxApp;
import com.lcsays.lcmall.db.model.WxMaUser;
import com.lcsays.lcmall.db.model.WxMarketingCoupon;
import com.lcsays.lcmall.db.service.WxMarketingCouponService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.card.*;
import me.chanjar.weixin.open.api.WxOpenMaService;
import org.springframework.web.bind.annotation.*;
import com.lcsays.lcmall.db.service.WxAppService;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @Author: lichuang
 * @Date: 21-10-8 12:28
 */
@Slf4j
@RestController
@RequestMapping("/api/wx/ma/{appId}/marketing")
public class WxMaMarketingController {

    @Resource
    WxPayService wxPayService;

    @Resource
    WxAppService wxAppService;

    @Resource
    WxMarketingCouponService marketingCouponService;

    @Resource
    WxMaService wxMaService;

    @Data
    private static class CreateCouponParam {
        private String stockId;
    }

    @Data
    private static class PostDemoParam {
        private String param1;
    }

    @PostMapping("/createCoupon")
    public BaseModel<FavorCouponsCreateResult> createCoupon(HttpSession session,
                                        @PathVariable String appId,
                                        @RequestBody CreateCouponParam createCouponParam) {
        /// mock
//        FavorCouponsCreateResult r = new FavorCouponsCreateResult();
//        r.setCouponId("27505904030");
//        return BaseModel.success(r);

        WxApp wxApp = wxAppService.queryByAppId(appId);
        if (null == wxApp) {
            return BaseModel.error(ErrorCode.PARAM_ERROR);
        }

        WxMaUser wxMaUser = SessionUtils.getWxUserFromSession(session);

        try {
            FavorCouponsCreateRequest request = new FavorCouponsCreateRequest();
            request.setStockId(createCouponParam.getStockId());
            String requestNo = RequestNo.randomWithCurTime("cc");
            request.setOutRequestNo(requestNo);
            request.setAppid(appId);
            String mchId = wxApp.getMchId();
            request.setStockCreatorMchid(mchId);
            FavorCouponsCreateResult result = wxPayService.switchoverTo(mchId).getMarketingFavorService()
                    .createFavorCouponsV3(wxMaUser.getOpenid(), request);
            log.info(result.toString());

            WxMarketingCoupon wxMarketingCoupon = new WxMarketingCoupon();
            wxMarketingCoupon.setWxAppId(wxApp.getId());
            wxMarketingCoupon.setWxMaUserId(wxMaUser.getId());
            wxMarketingCoupon.setStockId(createCouponParam.getStockId());
            wxMarketingCoupon.setCouponId(result.getCouponId());
            marketingCouponService.addCoupon(wxMarketingCoupon);
            return BaseModel.success(result);
        } catch (WxPayException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return BaseModel.errorWithMsg(ErrorCode.WX_SERVICE_ERROR, e.getMessage());
        }

    }

    @GetMapping("/coupon")
    public BaseModel<FavorCouponsGetResult> getCoupon(HttpSession session,
                                                            @PathVariable String appId,
                                                            @RequestParam("couponId") String couponId) {
        WxApp wxApp = wxAppService.queryByAppId(appId);
        if (null == wxApp) {
            return BaseModel.error(ErrorCode.PARAM_ERROR);
        }

        WxMaUser wxMaUser = SessionUtils.getWxUserFromSession(session);

        String mchId = wxApp.getMchId();
        try {
            FavorCouponsGetResult result = wxPayService.switchoverTo(mchId).getMarketingFavorService().getFavorCouponsV3(couponId, appId, wxMaUser.getOpenid());
            return BaseModel.success(result);
        } catch (WxPayException e) {
            e.printStackTrace();
            log.info(e.getMessage());
            return BaseModel.errorWithMsg(ErrorCode.WX_SERVICE_ERROR, e.getMessage());
        }
    }

    @GetMapping("/generateUrlLink")
    public BaseModel<String> generateUrlLink(@PathVariable String appId, String stockId) {
        try {
            GenerateUrlLinkRequest request = GenerateUrlLinkRequest.builder().build();
            request.setPath("/pages/stock/index");
            request.setQuery("?stockId=" + stockId);
            request.setIsExpire(false);
            String result = wxMaService.switchoverTo(appId).getLinkService().generateUrlLink(request);
            log.info(result);
            return BaseModel.success(result);
        } catch (WxErrorException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return BaseModel.errorWithMsg(ErrorCode.WX_SERVICE_ERROR, e.getMessage());
        }
    }

}
