package com.lcsays.lcmall.db.dao;

import com.lcsays.lcmall.db.model.WxMarketingActivityExtraGroup;
import com.lcsays.lcmall.db.model.WxMarketingActivityExtraGroupExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WxMarketingActivityExtraGroupMapper {
    long countByExample(WxMarketingActivityExtraGroupExample example);

    int deleteByExample(WxMarketingActivityExtraGroupExample example);

    int insert(WxMarketingActivityExtraGroup record);

    int insertSelective(WxMarketingActivityExtraGroup record);

    List<WxMarketingActivityExtraGroup> selectByExample(WxMarketingActivityExtraGroupExample example);

    int updateByExampleSelective(@Param("record") WxMarketingActivityExtraGroup record, @Param("example") WxMarketingActivityExtraGroupExample example);

    int updateByExample(@Param("record") WxMarketingActivityExtraGroup record, @Param("example") WxMarketingActivityExtraGroupExample example);
}