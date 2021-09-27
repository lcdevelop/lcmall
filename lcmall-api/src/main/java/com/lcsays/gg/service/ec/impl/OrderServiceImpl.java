package com.lcsays.gg.service.ec.impl;

import com.lcsays.gg.dao.ec.OrderDao;
import com.lcsays.gg.models.ec.Order;
import com.lcsays.gg.models.ec.OrderAffiliate;
import com.lcsays.gg.models.manager.WxApp;
import com.lcsays.gg.models.weixin.WxMaUser;
import com.lcsays.gg.service.ec.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-9-13 21:00
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Resource
    OrderDao orderDao;

    @Override
    public int createOrder(Order order) {
        return orderDao.createOrder(order);
    }

    @Override
    public int updateOrder(Order order) {
        return orderDao.updateOrder(order);
    }

    @Override
    public Order getOrderByOutTradeNo(String outTradeNo) {
        return orderDao.getOrderByOutTradeNo(outTradeNo);
    }

    @Override
    public int delOrderByOutTradeNo(WxMaUser wxMaUser, String outTradeNo) {
        return orderDao.delOrderByOutTradeNo(wxMaUser, outTradeNo);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderDao.getOrderById(orderId);
    }

    @Override
    public int insertOrderAffiliates(List<OrderAffiliate> orderAffiliates) {
        return orderDao.insertOrderAffiliates(orderAffiliates);
    }

    @Override
    public List<OrderAffiliate> getOrderAffiliatesByOrderId(Long orderId) {
        return orderDao.getOrderAffiliatesByOrderId(orderId);
    }

    @Override
    public List<Order> orderList(WxMaUser wxMaUser) {
        return orderDao.orderList(wxMaUser);
    }

    @Override
    public List<Order> getOrdersByAppId(WxApp wxApp, String outTradeNo) {
        return orderDao.getOrdersByAppId(wxApp, outTradeNo);
    }
}
