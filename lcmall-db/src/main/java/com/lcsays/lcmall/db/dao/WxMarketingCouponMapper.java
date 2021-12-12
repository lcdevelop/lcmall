package com.lcsays.lcmall.db.dao;

import com.lcsays.lcmall.db.model.WxMarketingCoupon;
import com.lcsays.lcmall.db.model.WxMarketingCouponExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WxMarketingCouponMapper {
    long countByExample(WxMarketingCouponExample example);

    int deleteByExample(WxMarketingCouponExample example);

    int insert(WxMarketingCoupon record);

    int insertSelective(WxMarketingCoupon record);

    List<WxMarketingCoupon> selectByExample(WxMarketingCouponExample example);

    int updateByExampleSelective(@Param("record") WxMarketingCoupon record, @Param("example") WxMarketingCouponExample example);

    int updateByExample(@Param("record") WxMarketingCoupon record, @Param("example") WxMarketingCouponExample example);
}