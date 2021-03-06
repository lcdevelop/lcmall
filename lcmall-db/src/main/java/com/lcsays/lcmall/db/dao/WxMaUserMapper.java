package com.lcsays.lcmall.db.dao;

import com.lcsays.lcmall.db.model.WxMaUser;
import com.lcsays.lcmall.db.model.WxMaUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WxMaUserMapper {
    long countByExample(WxMaUserExample example);

    int deleteByExample(WxMaUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(WxMaUser record);

    int insertSelective(WxMaUser record);

    List<WxMaUser> selectByExample(WxMaUserExample example);

    WxMaUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") WxMaUser record, @Param("example") WxMaUserExample example);

    int updateByExample(@Param("record") WxMaUser record, @Param("example") WxMaUserExample example);

    int updateByPrimaryKeySelective(WxMaUser record);

    int updateByPrimaryKey(WxMaUser record);
}