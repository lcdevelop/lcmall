package com.lcsays.gg.controller.weixin;

import com.google.common.collect.Lists;
import com.lcsays.gg.enums.ErrorCode;
import com.lcsays.gg.models.result.BaseModel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
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

    @Data
    private static class CreateCardParam {
        String couponId;
    }

    @PostMapping("/createCard")
    public BaseModel<WxMpCardCreateResult> createCard(HttpSession session,
                                                      @PathVariable String appId,
                                                      @RequestBody CreateCardParam createCardParam
    ) {
        log.info(appId);
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
            base.setLocationIdList(Lists.newArrayList("1234"));

            WxMpCardCreateRequest request = new WxMpCardCreateRequest();
            GeneralCouponCreateRequest generalCouponCreateRequest = new GeneralCouponCreateRequest();
            GeneralCoupon generalCoupon = new GeneralCoupon();
            generalCoupon.setBaseInfo(base);
            generalCoupon.setDefaultDetail("音乐木盒");
            generalCouponCreateRequest.setGeneralCoupon(generalCoupon);
            request.setCardCreateRequest(generalCouponCreateRequest);
            WxMpCardCreateResult result = wxMpService.switchoverTo(appId).getCardService().createCard(request);
            return BaseModel.success(result);
        } catch (WxErrorException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return BaseModel.errorWithMsg(ErrorCode.WX_SERVICE_ERROR, e.getMessage());
        }
    }
}
