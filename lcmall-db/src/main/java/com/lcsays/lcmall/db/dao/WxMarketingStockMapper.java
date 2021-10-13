package com.lcsays.lcmall.db.dao;

import com.lcsays.lcmall.db.model.WxMarketingStock;
import com.lcsays.lcmall.db.model.WxMarketingStockExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WxMarketingStockMapper {
    long countByExample(WxMarketingStockExample example);

    int deleteByExample(WxMarketingStockExample example);

    int insert(WxMarketingStock record);

    int insertSelective(WxMarketingStock record);

    List<WxMarketingStock> selectByExample(WxMarketingStockExample example);

    int updateByExampleSelective(@Param("record") WxMarketingStock record, @Param("example") WxMarketingStockExample example);

    int updateByExample(@Param("record") WxMarketingStock record, @Param("example") WxMarketingStockExample example);
}