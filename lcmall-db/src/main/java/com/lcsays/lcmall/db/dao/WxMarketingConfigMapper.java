package com.lcsays.lcmall.db.dao;

import com.lcsays.lcmall.db.model.WxMarketingConfig;
import com.lcsays.lcmall.db.model.WxMarketingConfigExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WxMarketingConfigMapper {
    long countByExample(WxMarketingConfigExample example);

    int deleteByExample(WxMarketingConfigExample example);

    int insert(WxMarketingConfig record);

    int insertSelective(WxMarketingConfig record);

    List<WxMarketingConfig> selectByExample(WxMarketingConfigExample example);

    int updateByExampleSelective(@Param("record") WxMarketingConfig record, @Param("example") WxMarketingConfigExample example);

    int updateByExample(@Param("record") WxMarketingConfig record, @Param("example") WxMarketingConfigExample example);
}