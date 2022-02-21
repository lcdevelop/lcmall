package com.lcsays.lcmall.db.dao;

import com.lcsays.lcmall.db.model.EcAddress;
import com.lcsays.lcmall.db.model.EcAddressExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EcAddressMapper {
    long countByExample(EcAddressExample example);

    int deleteByExample(EcAddressExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(EcAddress record);

    int insertSelective(EcAddress record);

    List<EcAddress> selectByExample(EcAddressExample example);

    EcAddress selectByPrimaryKey(Integer id);

    EcAddress selectByPrimaryKeyWithLogicalDelete(@Param("id") Integer id, @Param("andLogicalDeleted") boolean andLogicalDeleted);

    int updateByExampleSelective(@Param("record") EcAddress record, @Param("example") EcAddressExample example);

    int updateByExample(@Param("record") EcAddress record, @Param("example") EcAddressExample example);

    int updateByPrimaryKeySelective(EcAddress record);

    int updateByPrimaryKey(EcAddress record);

    int logicalDeleteByExample(@Param("example") EcAddressExample example);

    int logicalDeleteByPrimaryKey(Integer id);
}