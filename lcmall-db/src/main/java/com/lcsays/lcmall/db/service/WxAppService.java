package com.lcsays.lcmall.db.service;

import com.lcsays.lcmall.db.dao.WxAppMapper;
import com.lcsays.lcmall.db.model.WxApp;
import com.lcsays.lcmall.db.model.WxAppExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-9-27 16:10
 */
@Service
public class WxAppService {

    @Resource
    private WxAppMapper wxAppMapper;

    public List<WxApp> appList() {
        WxAppExample example = new WxAppExample();
        example.or().andAppIdIsNotNull();
        return wxAppMapper.selectByExample(example);
    }

    public WxApp queryById(Integer id) {
        WxAppExample example = new WxAppExample();
        WxAppExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        List<WxApp> ret = wxAppMapper.selectByExample(example);
        if (ret.size() > 0) {
            return ret.get(0);
        } else {
            return null;
        }
    }

    public WxApp queryByAppId(String appId) {
        WxAppExample example = new WxAppExample();
        WxAppExample.Criteria criteria = example.createCriteria();
        criteria.andAppIdEqualTo(appId);
        List<WxApp> ret = wxAppMapper.selectByExample(example);
        if (ret.size() > 0) {
            return ret.get(0);
        } else {
            return null;
        }
    }
}
