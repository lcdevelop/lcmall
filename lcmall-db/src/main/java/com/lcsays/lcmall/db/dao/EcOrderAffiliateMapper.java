package com.lcsays.lcmall.db.dao;

import com.lcsays.lcmall.db.model.EcOrderAffiliate;
import com.lcsays.lcmall.db.model.EcOrderAffiliateExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EcOrderAffiliateMapper {
    long countByExample(EcOrderAffiliateExample example);

    int deleteByExample(EcOrderAffiliateExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(EcOrderAffiliate record);

    int insertSelective(EcOrderAffiliate record);

    List<EcOrderAffiliate> selectByExample(EcOrderAffiliateExample example);

    EcOrderAffiliate selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") EcOrderAffiliate record, @Param("example") EcOrderAffiliateExample example);

    int updateByExample(@Param("record") EcOrderAffiliate record, @Param("example") EcOrderAffiliateExample example);

    int updateByPrimaryKeySelective(EcOrderAffiliate record);

    int updateByPrimaryKey(EcOrderAffiliate record);
}