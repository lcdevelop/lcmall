package com.lcsays.lcmall.api.controller.weixin;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.urllink.GenerateUrlLinkRequest;
import com.github.binarywang.wxpay.bean.marketing.FavorCouponsCreateRequest;
import com.github.binarywang.wxpay.bean.marketing.FavorCouponsCreateResult;
import com.github.binarywang.wxpay.bean.marketing.FavorCouponsGetResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.lcsays.lcmall.api.enums.ErrorCode;
import com.lcsays.lcmall.api.models.result.BaseModel;
import com.lcsays.lcmall.api.utils.RequestNo;
import com.lcsays.lcmall.api.utils.SessionUtils;
import com.lcsays.lcmall.db.model.WxApp;
import com.lcsays.lcmall.db.model.WxMaUser;
import com.lcsays.lcmall.db.model.WxMarketingCoupon;
import com.lcsays.lcmall.db.service.WxMarketingCouponService;
import com.lcsays.lcmall.db.service.WxMarketingWhitelistService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.core.env.Environment;
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
    Environment environment;

    @Resource
    WxPayService wxPayService;

    @Resource
    WxAppService wxAppService;

    @Resource
    WxMarketingCouponService marketingCouponService;

    @Resource
    WxMaService wxMaService;

    @Resource
    WxMarketingCouponService wxMarketingCouponService;

    @Resource
    WxMarketingWhitelistService wxMarketingWhitelistService;

    @Data
    private static class CreateCouponParam {
        private String stockId;

        /* 为了解决通过小程序schema链接带过来的参数总有？的问题 */
        public String getStockId() {
            return stockId.replace("?", "");
        }
    }

    @PostMapping("/createCoupon")
    public BaseModel<FavorCouponsCreateResult> createCoupon(HttpSession session,
                                                            @PathVariable String appId,
                                                            @RequestBody CreateCouponParam createCouponParam) {
        WxApp wxApp = wxAppService.queryByAppId(appId);
        if (null == wxApp) {
            return BaseModel.error(ErrorCode.PARAM_ERROR);
        }

        WxMaUser wxMaUser = SessionUtils.getWxUserFromSession(session);
        // 不在白名单里不允许领券
        if (!wxMarketingWhitelistService.contains(wxMaUser.getPhoneNumber())) {
            return BaseModel.errorWithMsg(ErrorCode.NO_RESULT, "非工行指定手机号用户无法领取");
        }

        if ("development".equals(environment.getProperty("env"))) {
            // 如果是开发环境就返回一个假的couponId
            String fakeCouponId = "27595222804";
            FavorCouponsCreateResult r = new FavorCouponsCreateResult();
            r.setCouponId(fakeCouponId);
            WxMarketingCoupon wxMarketingCoupon = new WxMarketingCoupon();
            wxMarketingCoupon.setWxAppId(wxApp.getId());
            wxMarketingCoupon.setWxMaUserId(wxMaUser.getId());
            wxMarketingCoupon.setStockId(createCouponParam.getStockId());
            wxMarketingCoupon.setCouponId(fakeCouponId);
            marketingCouponService.addCoupon(wxMarketingCoupon);
            return BaseModel.success(r);
        } else {


            try {
                FavorCouponsCreateRequest request = new FavorCouponsCreateRequest();
                request.setStockId(createCouponParam.getStockId());
                String requestNo = RequestNo.randomWithCurTime("cc");
                request.setOutRequestNo(requestNo);
                request.setAppid(appId);
                String mchId = wxApp.getMchId();
                request.setStockCreatorMchid(mchId);
                // 这里如果报错“商户号与appid不匹配”，一般是要在支付平台里配appid关联
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

    @GetMapping("/hasGotCoupon")
    public BaseModel<String> hasGotCoupon(HttpSession session, @RequestParam("stockId") String stockId) {
        WxMaUser wxMaUser = SessionUtils.getWxUserFromSession(session);

        WxMarketingCoupon wxMarketingCoupon = wxMarketingCouponService.queryByUserAndStock(wxMaUser, stockId);
        if (null == wxMarketingCoupon) {
            return BaseModel.error(ErrorCode.NO_RESULT);
        } else {
            return BaseModel.success();
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
            result = result.replaceAll("\"", "");
            log.info(result);
            return BaseModel.success(result);
        } catch (WxErrorException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return BaseModel.errorWithMsg(ErrorCode.WX_SERVICE_ERROR, e.getMessage());
        }
    }


}
