package com.lcsays.lcmall.db.dao;

import com.lcsays.lcmall.db.model.EcProduct;
import com.lcsays.lcmall.db.model.EcProductExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EcProductMapper {
    long countByExample(EcProductExample example);

    int deleteByExample(EcProductExample example);

    int insert(EcProduct record);

    int insertSelective(EcProduct record);

    List<EcProduct> selectByExample(EcProductExample example);

    int updateByExampleSelective(@Param("record") EcProduct record, @Param("example") EcProductExample example);

    int updateByExample(@Param("record") EcProduct record, @Param("example") EcProductExample example);
}