package com.lcsays.gg.dao.ec;

import com.lcsays.gg.models.ec.*;
import com.lcsays.gg.models.manager.WxApp;
import com.lcsays.gg.models.weixin.WxMaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-8-11 19:07
 */
@Mapper
public interface SkuDao {
    List<Sku> getSkusByAppId(WxApp wxApp, String name);
    Sku getSkuById(Long skuId);
    List<Sku> getSkusByCategoryId(Long categoryId);

    int createSku(@Param("sku") Sku sku);
    int updateSku(@Param("sku") Sku sku);
    int deleteSkuById(Long id);
}
