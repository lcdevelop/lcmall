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
public interface PriceDao {
    Price getPriceBySkuId(@Param("skuId") Long skuId);
    int createPrice(@Param("price") Price price);
    int updatePrice(@Param("price") Price price);
}
