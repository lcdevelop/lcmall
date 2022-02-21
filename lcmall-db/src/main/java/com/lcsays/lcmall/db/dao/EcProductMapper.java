package com.lcsays.lcmall.db.dao;

import com.lcsays.lcmall.db.model.EcProduct;
import com.lcsays.lcmall.db.model.EcProductExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EcProductMapper {
    long countByExample(EcProductExample example);

    int deleteByExample(EcProductExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(EcProduct record);

    int insertSelective(EcProduct record);

    List<EcProduct> selectByExample(EcProductExample example);

    EcProduct selectByPrimaryKey(Integer id);

    EcProduct selectByPrimaryKeyWithLogicalDelete(@Param("id") Integer id, @Param("andLogicalDeleted") boolean andLogicalDeleted);

    int updateByExampleSelective(@Param("record") EcProduct record, @Param("example") EcProductExample example);

    int updateByExample(@Param("record") EcProduct record, @Param("example") EcProductExample example);

    int updateByPrimaryKeySelective(EcProduct record);

    int updateByPrimaryKey(EcProduct record);

    int logicalDeleteByExample(@Param("example") EcProductExample example);

    int logicalDeleteByPrimaryKey(Integer id);
}