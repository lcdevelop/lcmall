package com.lcsays.lcmall.db.service;

import com.lcsays.lcmall.db.dao.WxMarketingCouponMapper;
import com.lcsays.lcmall.db.model.WxMarketingCoupon;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: lichuang
 * @Date: 21-9-27 16:10
 */
@Service
public class WxMarketingCouponService {

    @Resource
    private WxMarketingCouponMapper marketingCouponMapper;

    public int addCoupon(WxMarketingCoupon wxMarketingCoupon) {
        return marketingCouponMapper.insert(wxMarketingCoupon);
    }
}
