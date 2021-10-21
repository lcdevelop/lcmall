package com.lcsays.lcmall.api.dao.ec;

import com.lcsays.lcmall.api.models.ec.Price;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: lichuang
 * @Date: 21-8-11 19:07
 */
@Mapper
public interface PriceDao {
    Price getPriceBySkuId(@Param("skuId") Long skuId);
    int createPrice(@Param("price") Price price);
    int updatePrice(@Param("price") Price price);
}
