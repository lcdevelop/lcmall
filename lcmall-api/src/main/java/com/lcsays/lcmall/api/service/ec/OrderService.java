package com.lcsays.lcmall.api.service.ec;

import com.lcsays.lcmall.api.models.ec.Order;
import com.lcsays.lcmall.api.models.ec.OrderAffiliate;
import com.lcsays.lcmall.api.models.manager.WxApp;
import com.lcsays.lcmall.api.models.weixin.WxMaUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-9-13 21:00
 */

public interface OrderService {
    int createOrder(@Param("order") Order order);
    int updateOrder(@Param("order") Order order);
    Order getOrderByOutTradeNo(String outTradeNo);
    int delOrderByOutTradeNo(WxMaUser wxMaUser, String outTradeNo);
    Order getOrderById(Long orderId);
    int insertOrderAffiliates(List<OrderAffiliate> orderAffiliates);
    List<OrderAffiliate> getOrderAffiliatesByOrderId(Long orderId);
    List<Order> orderList(WxMaUser wxMaUser);
    List<Order> getOrdersByAppId(@Param("wxApp") WxApp wxApp, @Param("outTradeNo") String outTradeNo);
}
