package com.lcsays.lcmall.api.dao.ec;

import com.lcsays.lcmall.api.models.ec.Cart;
import com.lcsays.lcmall.api.models.weixin.WxMaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-8-11 19:07
 */
@Mapper
public interface CartDao {
    int addToCart(WxMaUser wxMaUser, Long skuId);
    Integer removeFromCart(WxMaUser wxMaUser, Long skuId);
    Integer getCountInCart(@Param("wxMaUser") WxMaUser wxMaUser);
    List<Cart> getCartsByUser(@Param("wxMaUser") WxMaUser wxMaUser);
}
