package com.lcsays.lcmall.api.service.gutgenius.impl;

import com.lcsays.lcmall.api.dao.gutgenius.GutGeniusDao;
import com.lcsays.lcmall.api.models.gutgenius.Intro;
import com.lcsays.lcmall.api.service.gutgenius.GutGeniusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-9-13 21:10
 */
@Service
@Slf4j
public class GutGeniusServiceImpl implements GutGeniusService {

    @Resource
    GutGeniusDao gutGeniusDao;

    @Override
    public List<Intro> getIntros() {
        return gutGeniusDao.getIntros();
    }
}
