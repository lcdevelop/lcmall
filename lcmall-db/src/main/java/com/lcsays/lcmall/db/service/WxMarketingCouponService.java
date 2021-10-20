package com.lcsays.lcmall.db.service;

import com.lcsays.lcmall.db.dao.WxMarketingCouponMapper;
import com.lcsays.lcmall.db.model.WxMaUser;
import com.lcsays.lcmall.db.model.WxMarketingCoupon;
import com.lcsays.lcmall.db.model.WxMarketingCouponExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-9-27 16:10
 */
@Service
public class WxMarketingCouponService {

    @Resource
    private WxMarketingCouponMapper marketingCouponMapper;

    public int addCoupon(WxMarketingCoupon wxMarketingCoupon) {
        wxMarketingCoupon.setCreateTime(new Date());
        return marketingCouponMapper.insert(wxMarketingCoupon);
    }
    
    public WxMarketingCoupon queryByUserAndStock(WxMaUser wxMaUser, String stockId) {
        WxMarketingCouponExample example = new WxMarketingCouponExample();
        WxMarketingCouponExample.Criteria criteria = example.createCriteria();
        criteria.andWxMaUserIdEqualTo(wxMaUser.getId());
        criteria.andStockIdEqualTo(stockId);
        List<WxMarketingCoupon> wxMarketingCoupons = marketingCouponMapper.selectByExample(example);
        if (wxMarketingCoupons.size() > 0) {
            return wxMarketingCoupons.get(0);
        } else {
            return null;
        }
    }

    public List<WxMarketingCoupon> queryAll() {
        WxMarketingCouponExample example = new WxMarketingCouponExample();
        WxMarketingCouponExample.Criteria criteria = example.createCriteria();
        criteria.andIdIsNotNull();
        return marketingCouponMapper.selectByExample(example);
    }

    public int update(WxMarketingCoupon record) {
        WxMarketingCouponExample example = new WxMarketingCouponExample();
        WxMarketingCouponExample.Criteria criteria = example.createCriteria();
        criteria.andStockIdEqualTo(record.getStockId());
        criteria.andCouponIdEqualTo(record.getCouponId());
        return marketingCouponMapper.updateByExampleSelective(record, example);
    }
}
