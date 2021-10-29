package com.lcsays.lcmall.db.service;

import com.lcsays.lcmall.db.dao.WxMarketingCouponMapper;
import com.lcsays.lcmall.db.model.WxMaUser;
import com.lcsays.lcmall.db.model.WxMarketingCoupon;
import com.lcsays.lcmall.db.model.WxMarketingCouponExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
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
    
    public List<WxMarketingCoupon> queryByUserAndStock(WxMaUser wxMaUser, String stockId) {
        WxMarketingCouponExample example = new WxMarketingCouponExample();
        WxMarketingCouponExample.Criteria criteria = example.createCriteria();
        criteria.andWxMaUserIdEqualTo(wxMaUser.getId());
        criteria.andStockIdEqualTo(stockId);
        return marketingCouponMapper.selectByExample(example);
    }

    public List<WxMarketingCoupon> queryByWxAppId(Integer wxAppId) {
        WxMarketingCouponExample example = new WxMarketingCouponExample();
        WxMarketingCouponExample.Criteria criteria = example.createCriteria();
        criteria.andWxAppIdEqualTo(wxAppId);
        return marketingCouponMapper.selectByExample(example);
    }

    public int update(WxMarketingCoupon record) {
        WxMarketingCouponExample example = new WxMarketingCouponExample();
        WxMarketingCouponExample.Criteria criteria = example.createCriteria();
        criteria.andStockIdEqualTo(record.getStockId());
        criteria.andCouponIdEqualTo(record.getCouponId());
        return marketingCouponMapper.updateByExampleSelective(record, example);
    }

    public List<WxMarketingCoupon> queryByStockIds(String[] stockIds) {
        WxMarketingCouponExample example = new WxMarketingCouponExample();
        example.setOrderByClause("`create_time` ASC");
        WxMarketingCouponExample.Criteria criteria = example.createCriteria();
        criteria.andStockIdIn(Arrays.asList(stockIds));
        criteria.andCreateTimeIsNotNull();
        return marketingCouponMapper.selectByExample(example);
    }
}
