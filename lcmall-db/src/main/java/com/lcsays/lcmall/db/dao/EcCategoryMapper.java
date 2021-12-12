package com.lcsays.lcmall.db.dao;

import com.lcsays.lcmall.db.model.EcCategory;
import com.lcsays.lcmall.db.model.EcCategoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EcCategoryMapper {
    long countByExample(EcCategoryExample example);

    int deleteByExample(EcCategoryExample example);

    int insert(EcCategory record);

    int insertSelective(EcCategory record);

    List<EcCategory> selectByExample(EcCategoryExample example);

    int updateByExampleSelective(@Param("record") EcCategory record, @Param("example") EcCategoryExample example);

    int updateByExample(@Param("record") EcCategory record, @Param("example") EcCategoryExample example);
}