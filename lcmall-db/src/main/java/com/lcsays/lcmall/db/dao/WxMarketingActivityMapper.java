package com.lcsays.lcmall.db.dao;

import com.lcsays.lcmall.db.model.WxMarketingActivity;
import com.lcsays.lcmall.db.model.WxMarketingActivityExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WxMarketingActivityMapper {
    long countByExample(WxMarketingActivityExample example);

    int deleteByExample(WxMarketingActivityExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(WxMarketingActivity record);

    int insertSelective(WxMarketingActivity record);

    List<WxMarketingActivity> selectByExample(WxMarketingActivityExample example);

    WxMarketingActivity selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") WxMarketingActivity record, @Param("example") WxMarketingActivityExample example);

    int updateByExample(@Param("record") WxMarketingActivity record, @Param("example") WxMarketingActivityExample example);

    int updateByPrimaryKeySelective(WxMarketingActivity record);

    int updateByPrimaryKey(WxMarketingActivity record);
}