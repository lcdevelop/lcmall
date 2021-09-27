package com.lcsays.gg.dao.ec;

import com.lcsays.gg.models.ec.Product;
import com.lcsays.gg.models.manager.WxApp;
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
    List<Product> getProducts(WxApp wxApp,
                              String name,
                              Integer categoryId);
    int createProduct(@Param("product") Product product);
    int updateProduct(@Param("product") Product product);
    int deleteProductById(Long id);
}
