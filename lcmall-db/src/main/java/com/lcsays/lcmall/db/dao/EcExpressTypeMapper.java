package com.lcsays.lcmall.db.dao;

import com.lcsays.lcmall.db.model.EcExpressType;
import com.lcsays.lcmall.db.model.EcExpressTypeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EcExpressTypeMapper {
    long countByExample(EcExpressTypeExample example);

    int deleteByExample(EcExpressTypeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(EcExpressType record);

    int insertSelective(EcExpressType record);

    List<EcExpressType> selectByExample(EcExpressTypeExample example);

    EcExpressType selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") EcExpressType record, @Param("example") EcExpressTypeExample example);

    int updateByExample(@Param("record") EcExpressType record, @Param("example") EcExpressTypeExample example);

    int updateByPrimaryKeySelective(EcExpressType record);

    int updateByPrimaryKey(EcExpressType record);
}