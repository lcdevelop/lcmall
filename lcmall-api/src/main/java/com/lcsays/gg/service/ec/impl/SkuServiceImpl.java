package com.lcsays.gg.service.ec.impl;

import com.lcsays.gg.dao.ec.*;
import com.lcsays.gg.models.ec.*;
import com.lcsays.gg.models.manager.WxApp;
import com.lcsays.gg.models.weixin.WxMaUser;
import com.lcsays.gg.service.ec.SkuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-9-13 19:59
 */
@Service
@Slf4j
public class SkuServiceImpl implements SkuService {

    @Resource
    PriceDao priceDao;

    @Resource
    CategoryDao categoryDao;

    @Resource
    ProductDao productDao;

    @Resource
    CartDao cartDao;

    @Resource
    SkuDao skuDao;

    @Override
    public Price getPriceBySkuId(Long skuId) {
        return priceDao.getPriceBySkuId(skuId);
    }

    @Override
    public int createPrice(Price price) {
        return priceDao.createPrice(price);
    }

    @Override
    public int updatePrice(Price price) {
        return priceDao.updatePrice(price);
    }

    @Override
    public List<Category> categoryList(WxApp wxApp) {
        return categoryDao.categoryList(wxApp);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryDao.getCategoryById(id);
    }

    @Override
    public int createCategory(Category category) {
        return categoryDao.createCategory(category);
    }

    @Override
    public int updateCategory(Category category) {
        return categoryDao.updateCategory(category);
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryDao.deleteCategoryById(id);
    }

    @Override
    public List<Product> getProductsByCategoryId(Long id) {
        return productDao.getProductsByCategoryId(id);
    }

    @Override
    public List<Product> getProducts(WxApp wxApp, String name, Integer categoryId) {
        return productDao.getProducts(wxApp, name, categoryId);
    }

    @Override
    public int createProduct(Product product) {
        return productDao.createProduct(product);
    }

    @Override
    public int updateProduct(Product product) {
        return productDao.updateProduct(product);
    }

    @Override
    public int deleteProductById(Long id) {
        return productDao.deleteProductById(id);
    }

    @Override
    public int addToCart(WxMaUser wxMaUser, Long skuId) {
        return cartDao.addToCart(wxMaUser, skuId);
    }

    @Override
    public Integer removeFromCart(WxMaUser wxMaUser, Long skuId) {
        return cartDao.removeFromCart(wxMaUser, skuId);
    }

    @Override
    public Integer getCountInCart(WxMaUser wxMaUser) {
        return cartDao.getCountInCart(wxMaUser);
    }

    @Override
    public List<Cart> getCartsByUser(WxMaUser wxMaUser) {
        return cartDao.getCartsByUser(wxMaUser);
    }

    @Override
    public List<Sku> getSkusByAppId(WxApp wxApp, String name) {
        return skuDao.getSkusByAppId(wxApp, name);
    }

    @Override
    public Sku getSkuById(Long skuId) {
        return skuDao.getSkuById(skuId);
    }

    @Override
    public List<Sku> getSkusByCategoryId(Long categoryId) {
        return skuDao.getSkusByCategoryId(categoryId);
    }

    @Override
    public int createSku(Sku sku) {
        return skuDao.createSku(sku);
    }

    @Override
    public int updateSku(Sku sku) {
        return skuDao.updateSku(sku);
    }

    @Override
    public int deleteSkuById(Long id) {
        return skuDao.deleteSkuById(id);
    }
}
