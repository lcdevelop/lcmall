package com.lcsays.gg.controller.weixin;

import com.google.common.collect.Lists;
import com.lcsays.gg.enums.ErrorCode;
import com.lcsays.gg.models.result.BaseModel;
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

    @PostMapping("/createCard")
    public BaseModel<WxMpCardCreateResult> createCard(HttpSession session, @PathVariable String appId) {
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
