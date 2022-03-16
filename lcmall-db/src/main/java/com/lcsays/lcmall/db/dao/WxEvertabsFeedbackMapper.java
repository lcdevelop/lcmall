package com.lcsays.lcmall.db.dao;

import com.lcsays.lcmall.db.model.WxEvertabsFeedback;
import com.lcsays.lcmall.db.model.WxEvertabsFeedbackExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WxEvertabsFeedbackMapper {
    long countByExample(WxEvertabsFeedbackExample example);

    int deleteByExample(WxEvertabsFeedbackExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(WxEvertabsFeedback record);

    int insertSelective(WxEvertabsFeedback record);

    List<WxEvertabsFeedback> selectByExample(WxEvertabsFeedbackExample example);

    WxEvertabsFeedback selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") WxEvertabsFeedback record, @Param("example") WxEvertabsFeedbackExample example);

    int updateByExample(@Param("record") WxEvertabsFeedback record, @Param("example") WxEvertabsFeedbackExample example);

    int updateByPrimaryKeySelective(WxEvertabsFeedback record);

    int updateByPrimaryKey(WxEvertabsFeedback record);
}