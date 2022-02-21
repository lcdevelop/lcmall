package com.lcsays.lcmall.db.dao;

import com.lcsays.lcmall.db.model.WxMarketingWhitelist;
import com.lcsays.lcmall.db.model.WxMarketingWhitelistExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WxMarketingWhitelistMapper {
    long countByExample(WxMarketingWhitelistExample example);

    int deleteByExample(WxMarketingWhitelistExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(WxMarketingWhitelist record);

    int insertSelective(WxMarketingWhitelist record);

    List<WxMarketingWhitelist> selectByExample(WxMarketingWhitelistExample example);

    WxMarketingWhitelist selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") WxMarketingWhitelist record, @Param("example") WxMarketingWhitelistExample example);

    int updateByExample(@Param("record") WxMarketingWhitelist record, @Param("example") WxMarketingWhitelistExample example);

    int updateByPrimaryKeySelective(WxMarketingWhitelist record);

    int updateByPrimaryKey(WxMarketingWhitelist record);
}