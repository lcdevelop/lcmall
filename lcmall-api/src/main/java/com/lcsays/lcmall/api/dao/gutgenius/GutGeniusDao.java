package com.lcsays.lcmall.api.dao.gutgenius;

import com.lcsays.lcmall.api.models.gutgenius.Intro;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-8-11 12:37
 */
@Mapper
public interface GutGeniusDao {
    List<Intro> getIntros();
}
