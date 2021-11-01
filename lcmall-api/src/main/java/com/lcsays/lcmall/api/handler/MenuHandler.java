package com.lcsays.lcmall.api.handler;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.lcsays.lcmall.api.utils.RequestNo;
import com.lcsays.lcmall.db.model.WxApp;
import com.lcsays.lcmall.db.service.WxAppService;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

import static me.chanjar.weixin.common.api.WxConsts.EventType;

@Component
public class MenuHandler extends AbstractHandler {

    @Resource
    WxAppService wxAppService;

    @Resource
    private WxPayService wxPayService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) {
        String msg = String.format("type:%s, event:%s, key:%s",
            wxMessage.getMsgType(), wxMessage.getEvent(),
            wxMessage.getEventKey());
        if (EventType.CLICK.equals(wxMessage.getEvent())) {
            // 根据是哪个公众号收到的消息来做处理
            String toUser = wxMessage.getToUser();
            WxApp wxApp = wxAppService.queryByOriginalId(toUser);
            // 找到wxapp了
            if (null != wxApp) {
                if ("testpay".equals(wxMessage.getEventKey())) {
                    // 测试支付1块钱
                    try {
                        WxPayUnifiedOrderV3Request request = new WxPayUnifiedOrderV3Request();
                        request.setAppid(wxApp.getAppId());
                        request.setMchid(wxApp.getMchId());
                        request.setDescription("this is Description");
                        request.setOutTradeNo(RequestNo.randomWithCurTime("wxpay"));
                        request.setNotifyUrl("https://cloud.lcsays.com/api/wx/" + wxApp.getAppId() + "/pay/order_notify");
                        WxPayUnifiedOrderV3Request.Amount amount = new WxPayUnifiedOrderV3Request.Amount();
                        amount.setTotal(610);
                        request.setAmount(amount);
                        String result = wxPayService.switchoverTo(wxApp.getMchId())
                                .createOrderV3(TradeTypeEnum.NATIVE, request);
                        return WxMpXmlOutMessage.TEXT().content(result)
                                .fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
                                .build();
                    } catch (WxPayException e) {
                        e.printStackTrace();
                        logger.error(e.getMessage());
                    }
                } else {
                    logger.warn("eventKey not found: " + wxMessage.getEventKey());
                }
            } else {
                logger.warn("OriginalId not found: " + toUser);
            }
            return null;
        }

        return WxMpXmlOutMessage.TEXT().content(msg)
            .fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
            .build();
    }

}
