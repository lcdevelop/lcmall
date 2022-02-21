package com.lcsays.lcmall.db.dao;

import com.lcsays.lcmall.db.model.WxEvertabsWorkspace;
import com.lcsays.lcmall.db.model.WxEvertabsWorkspaceExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WxEvertabsWorkspaceMapper {
    long countByExample(WxEvertabsWorkspaceExample example);

    int deleteByExample(WxEvertabsWorkspaceExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(WxEvertabsWorkspace record);

    int insertSelective(WxEvertabsWorkspace record);

    List<WxEvertabsWorkspace> selectByExample(WxEvertabsWorkspaceExample example);

    WxEvertabsWorkspace selectByPrimaryKey(Integer id);

    WxEvertabsWorkspace selectByPrimaryKeyWithLogicalDelete(@Param("id") Integer id, @Param("andLogicalDeleted") boolean andLogicalDeleted);

    int updateByExampleSelective(@Param("record") WxEvertabsWorkspace record, @Param("example") WxEvertabsWorkspaceExample example);

    int updateByExample(@Param("record") WxEvertabsWorkspace record, @Param("example") WxEvertabsWorkspaceExample example);

    int updateByPrimaryKeySelective(WxEvertabsWorkspace record);

    int updateByPrimaryKey(WxEvertabsWorkspace record);

    int logicalDeleteByExample(@Param("example") WxEvertabsWorkspaceExample example);

    int logicalDeleteByPrimaryKey(Integer id);
}