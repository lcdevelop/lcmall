package com.lcsays.lcmall.db.dao;

import com.lcsays.lcmall.db.model.WxMarketingActivityExtra;
import com.lcsays.lcmall.db.model.WxMarketingActivityExtraExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WxMarketingActivityExtraMapper {
    long countByExample(WxMarketingActivityExtraExample example);

    int deleteByExample(WxMarketingActivityExtraExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(WxMarketingActivityExtra record);

    int insertSelective(WxMarketingActivityExtra record);

    List<WxMarketingActivityExtra> selectByExample(WxMarketingActivityExtraExample example);

    WxMarketingActivityExtra selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") WxMarketingActivityExtra record, @Param("example") WxMarketingActivityExtraExample example);

    int updateByExample(@Param("record") WxMarketingActivityExtra record, @Param("example") WxMarketingActivityExtraExample example);

    int updateByPrimaryKeySelective(WxMarketingActivityExtra record);

    int updateByPrimaryKey(WxMarketingActivityExtra record);
}