package com.lcsays.lcmall.db.service;

import com.lcsays.lcmall.db.dao.EcOrderMapper;
import com.lcsays.lcmall.db.model.EcOrder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: lichuang
 * @Date: 21-9-27 16:10
 */
@Service
public class EcOrderService {

    @Resource
    EcOrderMapper orderMapper;

    public boolean createOrder(EcOrder order) {
        return orderMapper.insertSelective(order) > 0;
    }
}
