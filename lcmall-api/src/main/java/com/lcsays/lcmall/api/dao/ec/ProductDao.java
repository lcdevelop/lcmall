package com.lcsays.lcmall.api.dao.ec;

import com.lcsays.lcmall.api.models.ec.Product;
import com.lcsays.lcmall.api.models.manager.WxApp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-8-11 19:07
 */
@Mapper
public interface ProductDao {
    List<Product> getProductsByCategoryId(Long id);
    List<Product> getProducts(@Param("wxApp") WxApp wxApp,
                              @Param("name") String name,
                              @Param("categoryId") Integer categoryId);
    int createProduct(@Param("product") Product product);
    int updateProduct(@Param("product") Product product);
    int deleteProductById(Long id);
}
