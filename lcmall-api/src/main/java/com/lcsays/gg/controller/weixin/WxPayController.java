package com.lcsays.gg.controller.weixin;

import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyV3Result;
import com.github.binarywang.wxpay.bean.request.WxPayOrderQueryV3Request;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request.*;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.constant.WxPayErrorCode;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.lcsays.gg.enums.ErrorCode;
import com.lcsays.gg.enums.OrderStatus;
import com.lcsays.gg.models.ec.Address;
import com.lcsays.gg.models.ec.Order;
import com.lcsays.gg.models.ec.OrderAffiliate;
import com.lcsays.gg.models.ec.Sku;
import com.lcsays.gg.models.result.BaseModel;
import com.lcsays.gg.models.weixin.WxMaUser;
import com.lcsays.gg.service.ec.OrderService;
import com.lcsays.gg.service.weixin.UserService;
import com.lcsays.gg.utils.TimeUtils;
import com.lcsays.lcmall.db.model.WxApp;
import com.lcsays.lcmall.db.service.WxAppService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: lichuang
 * @Date: 21-8-5 18:47
 * 注解@RequestBody对应客户端要用postJson，@RequestParam对应客户端要用post/fetch
 */
@Slf4j
@RestController
@RequestMapping("/api/wx/{appId}/pay")
public class WxPayController {

    @Resource
    private WxPayService wxPayService;

    @Resource
    OrderService orderService;

    @Resource
    UserService userService;

    @Resource
    WxAppService wxAppService;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class CreateOrderParam {
        Long userId;
        Integer totalFee;
        Long addressId;
        List<CartParam> cartList;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class CartParam {
        private Long skuId;
        private Integer count;
        private Integer price;
    }

    @PostMapping("/create_order")
    public BaseModel<String> createOrder(@PathVariable String appId,
                                     @RequestBody CreateOrderParam createOrderParam
    ) {
        log.info(String.valueOf(createOrderParam));
        WxMaUser wxMaUser = userService.getWxMaUserById(appId, createOrderParam.getUserId());
        if (null != wxMaUser) {
            String outTradeNo = this.createOutTradeNo();
            Address address = userService.getAddressById(createOrderParam.getAddressId());
            if (null == address) {
                return BaseModel.error(ErrorCode.PARAM_ERROR);
            }
            Order order = new Order(wxMaUser.getWxApp(), wxMaUser, address, outTradeNo, createOrderParam.getTotalFee(), OrderStatus.OS_INIT.getValue());
            if (orderService.createOrder(order) > 0) {
                List<OrderAffiliate> orderAffiliates = new ArrayList<>();
                for (CartParam cartParam: createOrderParam.getCartList()) {
                    OrderAffiliate orderAffiliate = new OrderAffiliate(
                        order,
                        new Sku(cartParam.skuId),
                        cartParam.getCount(), cartParam.getPrice()
                    );
                    orderAffiliates.add(orderAffiliate);
                }
                int ret = orderService.insertOrderAffiliates(orderAffiliates);
                if (ret > 0) {
                    return BaseModel.success();
                } else {
                    return BaseModel.error(ErrorCode.DAO_ERROR);
                }
            } else {
                return BaseModel.error(ErrorCode.DAO_ERROR);
            }
        } else {
            return BaseModel.error(ErrorCode.NO_USER);
        }
    }

    @GetMapping("/order_list")
    public BaseModel<List<Order>> orderList(@PathVariable String appId,
                                                       @RequestParam("userId") Long userId) {
        WxMaUser wxMaUser = userService.getWxMaUserById(appId, userId);
        if (null != wxMaUser) {
            List<Order> orderList = orderService.orderList(wxMaUser);
            if (orderList.size() > 0) {
                return BaseModel.success(orderList);
            } else {
                return BaseModel.error(ErrorCode.NO_RESULT);
            }
        } else {
            return BaseModel.error(ErrorCode.NO_USER);
        }
    }

    @PostMapping("/del_order_by_no")
    public BaseModel<String> delOrderByOutTradeNo(@PathVariable String appId,
                                                                @RequestParam("userId") Long userId,
                                                                @RequestParam("outTradeNo") String outTradeNo) {
        WxMaUser wxMaUser = userService.getWxMaUserById(appId, userId);
        if (null != wxMaUser) {
            int ret = orderService.delOrderByOutTradeNo(wxMaUser, outTradeNo);
            if (ret > 0) {
                return BaseModel.success();
            } else {
                return BaseModel.error(ErrorCode.NO_RESULT);
            }
        } else {
            return BaseModel.error(ErrorCode.NO_USER);
        }
    }


    @GetMapping("/unified_order")
    public BaseModel<Map<String, String>> unifiedOrder(@PathVariable String appId,
                                                       @RequestParam("userId") Long userId,
                                                       @RequestParam("outTradeNo") String outTradeNo) {
        WxMaUser wxMaUser = userService.getWxMaUserById(appId, userId);
        if (null != wxMaUser) {
            try {
                Order order = orderService.getOrderByOutTradeNo(outTradeNo);
                if (null != order) {
                    WxPayUnifiedOrderV3Request request = new WxPayUnifiedOrderV3Request();

                    request.setDescription("这是商品描述");
                    request.setOutTradeNo(outTradeNo);
                    request.setNotifyUrl("https://www.lcsays.com/api/wx/" + appId + "/pay/order_notify");

                    Amount amount = new Amount();
                    amount.setTotal(order.getTotalFee());
                    amount.setCurrency("CNY");
                    request.setAmount(amount);

                    Payer payer = new Payer();
                    payer.setOpenid(wxMaUser.getOpenId());
                    request.setPayer(payer);

                    WxPayUnifiedOrderV3Result result = wxPayService.unifiedOrderV3(TradeTypeEnum.JSAPI, request);

                    String timeStamp = String.valueOf(new Date().getTime() / 1000);
                    String nonceStr = UUID.randomUUID().toString();
                    String packageStr = "prepay_id=" + result.getPrepayId();
                    Map<String, String> data = new HashMap<>();
                    data.put("appId", request.getAppid());
                    data.put("timeStamp", timeStamp);
                    data.put("nonceStr", nonceStr);
                    data.put("package", packageStr);
                    data.put("signType", "RSA");

                    // v3只支持RSA签名
                    String signature = getRSASignature(request.getAppid(), timeStamp, nonceStr, packageStr, wxPayService.getConfig().getPrivateKey());
                    data.put("paySign", signature);

                    return BaseModel.success(data);
                } else {
                    return BaseModel.error(ErrorCode.NO_RESULT);
                }
            } catch (WxPayException e) {
                e.printStackTrace();
                if (Objects.equals(e.getErrCode(), WxPayErrorCode.UnifiedOrder.ORDERPAID)) {
                    this.refreshOrderStatus(outTradeNo);
                    return BaseModel.error(ErrorCode.ORDER_PAID);
                } else {
                    return BaseModel.error(ErrorCode.WX_SERVICE_ERROR);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return BaseModel.error(ErrorCode.UNKNOWN_ERROR);
            }
        } else {
            return BaseModel.error(ErrorCode.NO_USER);
        }
    }

    @PostMapping("/order_notify")
    public String orderNotify(@PathVariable String appId,
                              @RequestHeader("wechatpay-timestamp") String timestamp,
                              @RequestHeader("wechatpay-nonce") String nonce,
                              @RequestHeader("wechatpay-signature") String signature,
                              @RequestHeader("wechatpay-serial") String serial,
                              @RequestBody String notifyData) {
        log.info(notifyData);

        WxApp wxApp = wxAppService.queryByAppId(appId);
        if (null != wxApp) {

            try {
                SignatureHeader header = new SignatureHeader();
                header.setTimeStamp(timestamp);
                header.setNonce(nonce);
                header.setSignature(signature);
                header.setSerial(serial);
                WxPayOrderNotifyV3Result result = wxPayService.switchoverTo(wxApp.getMchId())
                        .parseOrderNotifyV3Result(notifyData, header);
                String outTradeNo = result.getResult().getOutTradeNo();
                String transactionId = result.getResult().getTransactionId();
                String tradeState = result.getResult().getTradeState();
                String successTime = result.getResult().getSuccessTime();

                return this.updateOrderStatus(outTradeNo, transactionId, successTime, tradeState);
            } catch (WxPayException e) {
                e.printStackTrace();
                return WxPayNotifyResponse.fail(e.getMessage());
            }
        } else {
            return WxPayNotifyResponse.fail("no wxApp: " + appId);
        }
    }

    private String updateOrderStatus(String outTradeNo, String transactionId, String successTime, String tradeState) {
        Order order = orderService.getOrderByOutTradeNo(outTradeNo);
        if (null != order) {
            order.setTransactionId(transactionId);
            order.setSuccessTime(TimeUtils.timeStr2Timestamp(successTime.substring(0, 19).replace('T', ' ')));
            order.setTradeStatus(tradeState);
            if (tradeState.equals(WxPayConstants.WxpayTradeStatus.SUCCESS)) {
                order.setStatus(OrderStatus.OS_PAID.getValue());
            }
            if (orderService.updateOrder(order) > 0) {
                log.info("order update success: " + order);
                return WxPayNotifyResponse.success("成功");
            } else {
                log.error("order update error: " + order);
                return WxPayNotifyResponse.fail("业务订单更新失败");
            }
        } else {
            order = new Order();
            order.setOutTradeNo(outTradeNo);
            order.setTransactionId(transactionId);
            order.setSuccessTime(TimeUtils.timeStr2Timestamp(successTime.substring(0, 19).replace('T', ' ')));
            order.setTradeStatus(tradeState);
            if (tradeState.equals(WxPayConstants.WxpayTradeStatus.SUCCESS)) {
                order.setStatus(OrderStatus.OS_PAID.getValue());
            }
            if (orderService.createOrder(order) > 0) {
                return WxPayNotifyResponse.success("成功");
            } else {
                return WxPayNotifyResponse.fail("创建业务订单失败");
            }
        }
    }

    private String getRSASignature(String appId, String timeStamp, String nonceStr, String packageStr, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String signatureStr = Stream.of(appId, timeStamp, nonceStr, packageStr)
            .collect(Collectors.joining("\n", "", "\n"));
        log.info(signatureStr);

        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(privateKey);
        sign.update(signatureStr.getBytes(StandardCharsets.UTF_8));
        return Base64Utils.encodeToString(sign.sign());
    }

    private String createRandomNumWidth(int width) {
        Random random = new Random();
        String s = random.nextInt((int) (Math.pow(10, width) - 1)) + "";
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < width - s.length(); i++) {     //生成的随机数可能不是要求的7位的，所以不足的位数用0补齐，确保一定是7位的
            buffer.append("0");
        }
        return buffer + s;
    }

    private String createOutTradeNo() {
        return new Date().getTime() + this.createRandomNumWidth(3);
    }

    private String refreshOrderStatus(String outTradeNo) {
        try {
            WxPayOrderQueryV3Request request = new WxPayOrderQueryV3Request();
            request.setOutTradeNo(outTradeNo);
            WxPayOrderQueryV3Result result = wxPayService.queryOrderV3(request);
            log.info(result.toString());
            String transactionId = result.getTransactionId();
            String tradeState = result.getTradeState();
            String successTime = result.getSuccessTime();
            return this.updateOrderStatus(outTradeNo, transactionId, successTime, tradeState);
        } catch (WxPayException e) {
            e.printStackTrace();
            return WxPayNotifyResponse.fail(e.getMessage());
        }
    }
}

