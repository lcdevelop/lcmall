package com.lcsays.lcmall.api.service.ec;

import com.lcsays.lcmall.api.models.ec.*;
import com.lcsays.lcmall.api.models.manager.WxApp;
import com.lcsays.lcmall.api.models.weixin.WxMaUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-9-13 19:59
 */

public interface SkuService {
    Price getPriceBySkuId(Long skuId);
    int createPrice(Price price);
    int updatePrice(Price price);

    List<Category> categoryList(WxApp wxApp);
    Category getCategoryById(Long id);
    int createCategory(Category category);
    int updateCategory(Category category);
    void deleteCategoryById(Long id);

    List<Product> getProductsByCategoryId(Long id);
    List<Product> getProducts(WxApp wxApp,
                              String name,
                              Integer categoryId);
    int createProduct(Product product);
    int updateProduct(Product product);
    int deleteProductById(Long id);

    int addToCart(WxMaUser wxMaUser, Long skuId);
    Integer removeFromCart(WxMaUser wxMaUser, Long skuId);
    Integer getCountInCart(WxMaUser wxMaUser);
    List<Cart> getCartsByUser(WxMaUser wxMaUser);

    List<Sku> getSkusByAppId(WxApp wxApp, String name);
    Sku getSkuById(Long skuId);
    List<Sku> getSkusByCategoryId(Long categoryId);

    int createSku(@Param("sku") Sku sku);
    int updateSku(@Param("sku") Sku sku);
    int deleteSkuById(Long id);
}
