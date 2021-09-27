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
public interface CategoryDao {
    List<Category> categoryList(@Param("wxApp") WxApp wxApp);
    Category getCategoryById(Long id);
    int createCategory(@Param("category") Category category);
    int updateCategory(@Param("category") Category category);
    void deleteCategoryById(Long id);
}
