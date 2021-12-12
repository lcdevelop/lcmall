package com.lcsays.lcmall.db.dao;

import com.lcsays.lcmall.db.model.EcPrice;
import com.lcsays.lcmall.db.model.EcPriceExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EcPriceMapper {
    long countByExample(EcPriceExample example);

    int deleteByExample(EcPriceExample example);

    int insert(EcPrice record);

    int insertSelective(EcPrice record);

    List<EcPrice> selectByExample(EcPriceExample example);

    int updateByExampleSelective(@Param("record") EcPrice record, @Param("example") EcPriceExample example);

    int updateByExample(@Param("record") EcPrice record, @Param("example") EcPriceExample example);
}