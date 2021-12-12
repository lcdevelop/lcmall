package com.lcsays.lcmall.db.dao;

import com.lcsays.lcmall.db.model.EcSku;
import com.lcsays.lcmall.db.model.EcSkuExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EcSkuMapper {
    long countByExample(EcSkuExample example);

    int deleteByExample(EcSkuExample example);

    int insert(EcSku record);

    int insertSelective(EcSku record);

    List<EcSku> selectByExample(EcSkuExample example);

    int updateByExampleSelective(@Param("record") EcSku record, @Param("example") EcSkuExample example);

    int updateByExample(@Param("record") EcSku record, @Param("example") EcSkuExample example);
}