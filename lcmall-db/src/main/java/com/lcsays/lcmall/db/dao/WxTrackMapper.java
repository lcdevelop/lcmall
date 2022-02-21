package com.lcsays.lcmall.db.dao;

import com.lcsays.lcmall.db.model.WxTrack;
import com.lcsays.lcmall.db.model.WxTrackExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WxTrackMapper {
    long countByExample(WxTrackExample example);

    int deleteByExample(WxTrackExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(WxTrack record);

    int insertSelective(WxTrack record);

    List<WxTrack> selectByExample(WxTrackExample example);

    WxTrack selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") WxTrack record, @Param("example") WxTrackExample example);

    int updateByExample(@Param("record") WxTrack record, @Param("example") WxTrackExample example);

    int updateByPrimaryKeySelective(WxTrack record);

    int updateByPrimaryKey(WxTrack record);
}