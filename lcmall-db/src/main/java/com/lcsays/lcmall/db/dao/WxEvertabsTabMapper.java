package com.lcsays.lcmall.db.dao;

import com.lcsays.lcmall.db.model.WxEvertabsTab;
import com.lcsays.lcmall.db.model.WxEvertabsTabExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WxEvertabsTabMapper {
    long countByExample(WxEvertabsTabExample example);

    int deleteByExample(WxEvertabsTabExample example);

    int deleteByPrimaryKey(Integer pkId);

    int insert(WxEvertabsTab record);

    int insertSelective(WxEvertabsTab record);

    List<WxEvertabsTab> selectByExample(WxEvertabsTabExample example);

    WxEvertabsTab selectByPrimaryKey(Integer pkId);

    WxEvertabsTab selectByPrimaryKeyWithLogicalDelete(@Param("pkId") Integer pkId, @Param("andLogicalDeleted") boolean andLogicalDeleted);

    int updateByExampleSelective(@Param("record") WxEvertabsTab record, @Param("example") WxEvertabsTabExample example);

    int updateByExample(@Param("record") WxEvertabsTab record, @Param("example") WxEvertabsTabExample example);

    int updateByPrimaryKeySelective(WxEvertabsTab record);

    int updateByPrimaryKey(WxEvertabsTab record);

    int logicalDeleteByExample(@Param("example") WxEvertabsTabExample example);

    int logicalDeleteByPrimaryKey(Integer pkId);
}