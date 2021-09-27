package com.lcsays.gg.dao.ec;

import com.lcsays.gg.models.ec.Cart;
import com.lcsays.gg.models.weixin.WxMaUser;
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
