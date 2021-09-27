package com.lcsays.lcmall.db.dao;

import com.lcsays.lcmall.db.model.EcOrder;
import com.lcsays.lcmall.db.model.EcOrderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EcOrderMapper {
    long countByExample(EcOrderExample example);

    int deleteByExample(EcOrderExample example);

    int insert(EcOrder record);

    int insertSelective(EcOrder record);

    List<EcOrder> selectByExample(EcOrderExample example);

    int updateByExampleSelective(@Param("record") EcOrder record, @Param("example") EcOrderExample example);

    int updateByExample(@Param("record") EcOrder record, @Param("example") EcOrderExample example);
}