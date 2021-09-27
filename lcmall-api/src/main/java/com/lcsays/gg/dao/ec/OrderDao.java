package com.lcsays.gg.dao.ec;

import com.lcsays.gg.models.ec.*;
import com.lcsays.gg.models.manager.WxApp;
import com.lcsays.gg.models.weixin.WxMaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-8-11 19:07
 */
@Mapper
public interface OrderDao {
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
