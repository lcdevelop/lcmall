package com.lcsays.lcmall.db.dao;

import com.lcsays.lcmall.db.model.EcCart;
import com.lcsays.lcmall.db.model.EcCartExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EcCartMapper {
    long countByExample(EcCartExample example);

    int deleteByExample(EcCartExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(EcCart record);

    int insertSelective(EcCart record);

    List<EcCart> selectByExample(EcCartExample example);

    EcCart selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") EcCart record, @Param("example") EcCartExample example);

    int updateByExample(@Param("record") EcCart record, @Param("example") EcCartExample example);

    int updateByPrimaryKeySelective(EcCart record);

    int updateByPrimaryKey(EcCart record);
}