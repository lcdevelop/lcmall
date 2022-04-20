package com.lcsays.lcmall.db.dao;

import com.lcsays.lcmall.db.model.WxEvertabsTrack;
import com.lcsays.lcmall.db.model.WxEvertabsTrackExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WxEvertabsTrackMapper {
    long countByExample(WxEvertabsTrackExample example);

    int deleteByExample(WxEvertabsTrackExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(WxEvertabsTrack record);

    int insertSelective(WxEvertabsTrack record);

    List<WxEvertabsTrack> selectByExample(WxEvertabsTrackExample example);

    WxEvertabsTrack selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") WxEvertabsTrack record, @Param("example") WxEvertabsTrackExample example);

    int updateByExample(@Param("record") WxEvertabsTrack record, @Param("example") WxEvertabsTrackExample example);

    int updateByPrimaryKeySelective(WxEvertabsTrack record);

    int updateByPrimaryKey(WxEvertabsTrack record);
}