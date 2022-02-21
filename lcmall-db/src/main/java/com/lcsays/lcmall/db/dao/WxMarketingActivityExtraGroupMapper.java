package com.lcsays.lcmall.db.dao;

import com.lcsays.lcmall.db.model.WxMarketingActivityExtraGroup;
import com.lcsays.lcmall.db.model.WxMarketingActivityExtraGroupExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WxMarketingActivityExtraGroupMapper {
    long countByExample(WxMarketingActivityExtraGroupExample example);

    int deleteByExample(WxMarketingActivityExtraGroupExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(WxMarketingActivityExtraGroup record);

    int insertSelective(WxMarketingActivityExtraGroup record);

    List<WxMarketingActivityExtraGroup> selectByExample(WxMarketingActivityExtraGroupExample example);

    WxMarketingActivityExtraGroup selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") WxMarketingActivityExtraGroup record, @Param("example") WxMarketingActivityExtraGroupExample example);

    int updateByExample(@Param("record") WxMarketingActivityExtraGroup record, @Param("example") WxMarketingActivityExtraGroupExample example);

    int updateByPrimaryKeySelective(WxMarketingActivityExtraGroup record);

    int updateByPrimaryKey(WxMarketingActivityExtraGroup record);
}