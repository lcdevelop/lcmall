package com.lcsays.lcmall.db.dao;

import com.lcsays.lcmall.db.model.WxApp;
import com.lcsays.lcmall.db.model.WxAppExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WxAppMapper {
    long countByExample(WxAppExample example);

    int deleteByExample(WxAppExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(WxApp record);

    int insertSelective(WxApp record);

    List<WxApp> selectByExample(WxAppExample example);

    WxApp selectByPrimaryKey(Integer id);

    WxApp selectByPrimaryKeyWithLogicalDelete(@Param("id") Integer id, @Param("andLogicalDeleted") boolean andLogicalDeleted);

    int updateByExampleSelective(@Param("record") WxApp record, @Param("example") WxAppExample example);

    int updateByExample(@Param("record") WxApp record, @Param("example") WxAppExample example);

    int updateByPrimaryKeySelective(WxApp record);

    int updateByPrimaryKey(WxApp record);

    int logicalDeleteByExample(@Param("example") WxAppExample example);

    int logicalDeleteByPrimaryKey(Integer id);
}