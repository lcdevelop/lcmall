package com.lcsays.lcmall.db.dao;

import com.lcsays.lcmall.db.model.GgIntro;
import com.lcsays.lcmall.db.model.GgIntroExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GgIntroMapper {
    long countByExample(GgIntroExample example);

    int deleteByExample(GgIntroExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(GgIntro record);

    int insertSelective(GgIntro record);

    List<GgIntro> selectByExampleWithBLOBs(GgIntroExample example);

    List<GgIntro> selectByExample(GgIntroExample example);

    GgIntro selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") GgIntro record, @Param("example") GgIntroExample example);

    int updateByExampleWithBLOBs(@Param("record") GgIntro record, @Param("example") GgIntroExample example);

    int updateByExample(@Param("record") GgIntro record, @Param("example") GgIntroExample example);

    int updateByPrimaryKeySelective(GgIntro record);

    int updateByPrimaryKeyWithBLOBs(GgIntro record);

    int updateByPrimaryKey(GgIntro record);
}