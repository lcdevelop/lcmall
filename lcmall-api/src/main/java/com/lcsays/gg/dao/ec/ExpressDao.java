package com.lcsays.gg.dao.ec;

import com.lcsays.gg.models.ec.ExpressType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * @Author: lichuang
 * @Date: 21-8-11 19:07
 */
@Mapper
public interface ExpressDao {
    List<ExpressType> getAllExpressTypes();
    ExpressType getExpressTypeById(Long id);
}
