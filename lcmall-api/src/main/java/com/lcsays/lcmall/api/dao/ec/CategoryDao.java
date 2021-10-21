package com.lcsays.lcmall.api.dao.ec;

import com.lcsays.lcmall.api.models.ec.Category;
import com.lcsays.lcmall.api.models.manager.WxApp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-8-11 19:07
 */
@Mapper
public interface CategoryDao {
    List<Category> categoryList(@Param("wxApp") WxApp wxApp);
    Category getCategoryById(Long id);
    int createCategory(@Param("category") Category category);
    int updateCategory(@Param("category") Category category);
    void deleteCategoryById(Long id);
}
