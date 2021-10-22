package com.lcsays.lcmall.api.controller.weixin;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.urllink.GenerateUrlLinkRequest;
import com.github.binarywang.wxpay.bean.marketing.FavorCouponsCreateRequest;
import com.github.binarywang.wxpay.bean.marketing.FavorCouponsCreateResult;
import com.github.binarywang.wxpay.bean.marketing.FavorCouponsGetResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.lcsays.lcmall.api.enums.ErrorCode;
import com.lcsays.lcmall.api.models.dbwrapper.WxMarketingActivityExtraGroupEx;
import com.lcsays.lcmall.api.models.result.BaseModel;
import com.lcsays.lcmall.api.utils.RequestNo;
import com.lcsays.lcmall.api.utils.SessionUtils;
import com.lcsays.lcmall.db.model.*;
import com.lcsays.lcmall.db.service.*;
import com.lcsays.lcmall.db.util.WxMarketingActivityUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Resource
    WxMarketingActivityService wxMarketingActivityService;

    @Resource
    WxMarketingActivityExtraService wxMarketingActivityExtraService;

    @Resource
    WxMarketingActivityExtraGroupService wxMarketingActivityExtraGroupService;

    @Resource
    WxMarketingStockService wxMarketingStockService;

    @Data
    private static class CreateCouponParam {
        private Integer activityId;

        /* 为了解决通过小程序schema链接带过来的参数总有？的问题 */
//        public String getStockId() {
//            return stockId.replace("?", "");
//        }
    }

    @GetMapping("/activityExtras")
    public BaseModel<List<WxMarketingActivityExtraGroupEx>> activityExtras(@RequestParam Integer activityId) {
        List<WxMarketingActivityExtraGroup> groups = wxMarketingActivityExtraGroupService.queryByActivityId(activityId);
        Map<Integer, WxMarketingActivityExtraGroup> idGroupMap = new HashMap<>();
        for (WxMarketingActivityExtraGroup group: groups) {
            idGroupMap.put(group.getId(), group);
        }
        List<Integer> groupIds = groups.stream().map(WxMarketingActivityExtraGroup::getId).collect(Collectors.toList());
        Map<Integer, WxMarketingActivityExtraGroupEx> idGroupExMap = new HashMap<>();
        for (WxMarketingActivityExtra extra: wxMarketingActivityExtraService.queryByGroupIds(groupIds)) {
            if (idGroupExMap.containsKey(extra.getGroupId())) {
                idGroupExMap.get(extra.getGroupId()).getExtras().add(extra);
            } else {
                WxMarketingActivityExtraGroupEx groupEx = new WxMarketingActivityExtraGroupEx();
                groupEx.copyFrom(idGroupMap.get(extra.getGroupId()));
                List<WxMarketingActivityExtra> extras = new ArrayList<>();
                extras.add(extra);
                groupEx.setExtras(extras);
                idGroupExMap.put(extra.getGroupId(), groupEx);
            }
        }

        return BaseModel.success(new ArrayList<>(idGroupExMap.values()));
    }

    @PostMapping("/createCoupon")
    public BaseModel<List<FavorCouponsCreateResult>> createCoupon(HttpSession session,
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

//        if ("development".equals(environment.getProperty("env"))) {
//            // 如果是开发环境就返回一个假的couponId
//            String fakeCouponId = "27595222804";
//            FavorCouponsCreateResult r = new FavorCouponsCreateResult();
//            r.setCouponId(fakeCouponId);
//            WxMarketingCoupon wxMarketingCoupon = new WxMarketingCoupon();
//            wxMarketingCoupon.setWxAppId(wxApp.getId());
//            wxMarketingCoupon.setWxMaUserId(wxMaUser.getId());
//            wxMarketingCoupon.setStockId(createCouponParam.getStockId());
//            wxMarketingCoupon.setCouponId(fakeCouponId);
//            marketingCouponService.addCoupon(wxMarketingCoupon);
//            return BaseModel.success(r);
//        } else {





        WxMarketingActivity activity = wxMarketingActivityService.queryById(createCouponParam.getActivityId());
        log.info(activity.toString());

        List<FavorCouponsCreateResult> ret = new ArrayList<>();

        for (String stockId: WxMarketingActivityUtil.getStockIds(activity)) {
            // 如果已经领过的则直接跳过
            if (null != wxMarketingCouponService.queryByUserAndStock(wxMaUser, stockId)) {
                continue;
            }

            try {
                FavorCouponsCreateRequest request = new FavorCouponsCreateRequest();
                request.setStockId(stockId);
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
                wxMarketingCoupon.setStockId(stockId);
                wxMarketingCoupon.setCouponId(result.getCouponId());
                marketingCouponService.addCoupon(wxMarketingCoupon);

                ret.add(result);
            } catch (WxPayException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                return BaseModel.errorWithMsg(ErrorCode.WX_SERVICE_ERROR, e.getMessage());
            }
        }

        return BaseModel.success(ret);





//        }
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
    public BaseModel<String> hasGotCoupon(HttpSession session, @RequestParam("activityId") Integer activityId) {
        WxMaUser wxMaUser = SessionUtils.getWxUserFromSession(session);

        WxMarketingActivity activity = wxMarketingActivityService.queryById(activityId);
        for (String stockId: WxMarketingActivityUtil.getStockIds(activity)) {
            if (null == wxMarketingCouponService.queryByUserAndStock(wxMaUser, stockId)) {
                return BaseModel.error(ErrorCode.NO_RESULT);
            }
        }
        return BaseModel.success();
    }

    @GetMapping("/generateUrlLink")
    public BaseModel<String> generateUrlLink(@PathVariable String appId,
                                             @RequestParam Integer activityId,
                                             @RequestParam Integer templateType) {
        WxMarketingActivity activity = wxMarketingActivityService.queryById(activityId);
        if (null == activity) {
            return BaseModel.error(ErrorCode.PARAM_ERROR);
        }

        for (String stockId: WxMarketingActivityUtil.getStockIds(activity)) {
            WxMarketingStock stock = wxMarketingStockService.queryByStockId(stockId);
            if (StringUtils.isEmpty(stock.getCardId())) {
                return BaseModel.errorWithMsg(ErrorCode.NO_RESULT, "还没填写CardId,stockId:" + stockId);
            }
        }

        try {
            GenerateUrlLinkRequest request = GenerateUrlLinkRequest.builder().build();
            request.setPath("/pages/stock/index");
            request.setQuery("?activityId=" + activityId + "&templateType=" + templateType);
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
