package com.lcsays.lcmall.api.dao.ec;

import com.lcsays.lcmall.api.models.ec.Sku;
import com.lcsays.lcmall.api.models.manager.WxApp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-8-11 19:07
 */
@Mapper
public interface SkuDao {
    List<Sku> getSkusByAppId(@Param("wxApp") WxApp wxApp, @Param("name") String name);
    Sku getSkuById(Long skuId);
    List<Sku> getSkusByCategoryId(Long categoryId);

    int createSku(@Param("sku") Sku sku);
    int updateSku(@Param("sku") Sku sku);
    int deleteSkuById(Long id);
}
