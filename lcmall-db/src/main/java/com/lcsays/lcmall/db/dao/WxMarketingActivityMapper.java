package com.lcsays.lcmall.db.dao;

import com.lcsays.lcmall.db.model.WxMarketingActivity;
import com.lcsays.lcmall.db.model.WxMarketingActivityExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WxMarketingActivityMapper {
    long countByExample(WxMarketingActivityExample example);

    int deleteByExample(WxMarketingActivityExample example);

    int insert(WxMarketingActivity record);

    int insertSelective(WxMarketingActivity record);

    List<WxMarketingActivity> selectByExample(WxMarketingActivityExample example);

    int updateByExampleSelective(@Param("record") WxMarketingActivity record, @Param("example") WxMarketingActivityExample example);

    int updateByExample(@Param("record") WxMarketingActivity record, @Param("example") WxMarketingActivityExample example);
}