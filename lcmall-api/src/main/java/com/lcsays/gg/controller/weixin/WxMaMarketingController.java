package com.lcsays.gg.controller.weixin;

import com.github.binarywang.wxpay.bean.marketing.FavorCouponsCreateRequest;
import com.github.binarywang.wxpay.bean.marketing.FavorCouponsCreateResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.google.common.collect.Lists;
import com.lcsays.gg.enums.ErrorCode;
import com.lcsays.gg.models.result.BaseModel;
import com.lcsays.gg.models.weixin.WxMaUser;
import com.lcsays.gg.utils.RequestNo;
import com.lcsays.gg.utils.SessionUtils;
import com.lcsays.lcmall.db.model.WxApp;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.card.*;
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
    WxMpService wxMpService;

    @Resource
    WxAppService wxAppService;

    @Data
    private static class CreateCouponParam {
        private String stockId;
    }

    @Data
    private static class PostDemoParam {
        private String param1;
    }

    @GetMapping("/getDemo")
    public BaseModel<String> getDemo(HttpSession session, @RequestParam("param1") String param1) {
        return BaseModel.success(param1);
    }

    @PostMapping("/postDemo")
    public BaseModel<String> postDemo(HttpSession session, @RequestBody PostDemoParam postDemoParam) {
        return BaseModel.success(postDemoParam.getParam1());
    }

    @PostMapping("/createCoupon")
    public BaseModel<FavorCouponsCreateResult> createCoupon(HttpSession session,
                                        @PathVariable String appId,
                                        @RequestBody CreateCouponParam createCouponParam) {
        /// mock
        FavorCouponsCreateResult r = new FavorCouponsCreateResult();
        r.setCouponId("27505904030");
        return BaseModel.success(r);

//        WxApp wxApp = wxAppService.queryByAppId(appId);
//        if (null == wxApp) {
//            return BaseModel.error(ErrorCode.PARAM_ERROR);
//        }
//
//        WxMaUser wxMaUser = SessionUtils.getUserFromSession(session);
//
//        try {
//            FavorCouponsCreateRequest request = new FavorCouponsCreateRequest();
//            request.setStockId(createCouponParam.getStockId());
//            String requestNo = RequestNo.randomWithCurTime("cc");
//            log.info(requestNo);
//            request.setOutRequestNo(requestNo);
//            request.setAppid(appId);
//            String mchId = wxApp.getMchId();
//            request.setStockCreatorMchid(mchId);
//            FavorCouponsCreateResult result = wxPayService.switchoverTo(mchId).getMarketingFavorService()
//                    .createFavorCouponsV3(wxMaUser.getOpenId(), request);
//            log.info(result.toString());
//            return BaseModel.success(result);
//        } catch (WxPayException e) {
//            e.printStackTrace();
//            log.error(e.getMessage());
//            return BaseModel.errorWithMsg(ErrorCode.WX_SERVICE_ERROR, e.getMessage());
//        }

    }

    @PostMapping("/createCard")
    public BaseModel<WxMpCardCreateResult> createCard(HttpSession session, @PathVariable String appId) {
        try {
            BaseInfo base = new BaseInfo();
            base.setLogoUrl("http://mmbiz.qpic.cn/mmbiz/iaL1LJM1mF9aRKPZJkmG8xXhiaHqkKSVMMWeN3hLut7X7hicFNjakmxibMLGWpXrEXB33367o7zHN0CwngnQY7zb7g/0");
            base.setBrandName("测试优惠券");
            base.setCodeType("CODE_TYPE_QRCODE");
            base.setTitle("测试标题");
            base.setColor("Color010");
            base.setNotice("测试Notice");
            base.setServicePhone("020-88888888");
            base.setDescription("不可与其他优惠同享\\n如需团购券发票，请在消费时向商户提出\\n店内均可使用，仅限堂食");
            DateInfo info = new DateInfo();
            info.setType("DATE_TYPE_FIX_TERM");
            info.setFixedBeginTerm(0);
            info.setFixedTerm(30);
            base.setDateInfo(info);
            Sku sku = new Sku();
            sku.setQuantity(100);
            base.setSku(sku);
            base.setGetLimit(1);
            base.setCanShare(true);
            base.setCanGiveFriend(true);
            base.setUseAllLocations(true);
            base.setCenterTitle("顶部居中按钮");
            base.setCenterSubTitle("按钮下方的wording");
            base.setCenterUrl("www.qq.com");
            base.setCustomUrl("http://www.qq.com");
            base.setCustomUrlName("立即使用");
            base.setCustomUrlSubTitle("副标题tip");
            base.setPromotionUrlName("更多优惠");
            base.setPromotionUrl("http://www.qq.com");
            base.setLocationIdList(Lists.newArrayList("1234"));

            WxMpCardCreateRequest request = new WxMpCardCreateRequest();
            DiscountCardCreateRequest discountCardCreateRequest = new DiscountCardCreateRequest();
            DiscountCard discountCard = new DiscountCard();
            discountCard.setBaseInfo(base);
            discountCard.setDiscount(30);
            discountCardCreateRequest.setDiscount(discountCard);
            request.setCardCreateRequest(discountCardCreateRequest);
            WxMpCardCreateResult result = wxMpService.switchoverTo(appId).getCardService().createCard(request);
            return BaseModel.success(result);
        } catch (WxErrorException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return BaseModel.errorWithMsg(ErrorCode.WX_SERVICE_ERROR, e.getMessage());
        }
    }
}
