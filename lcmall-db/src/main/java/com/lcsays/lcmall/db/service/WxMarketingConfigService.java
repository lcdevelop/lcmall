package com.lcsays.lcmall.db.service;

import com.lcsays.lcmall.db.dao.WxMarketingConfigMapper;
import com.lcsays.lcmall.db.model.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-9-27 16:10
 */
@Service
public class WxMarketingConfigService {

    @Resource
    private WxMarketingConfigMapper wxMarketingConfigMapper;

    public int createOrUpdate(WxMarketingConfig wxMarketingConfig) {
        WxMarketingConfigExample example = new WxMarketingConfigExample();
        WxMarketingConfigExample.Criteria criteria = example.createCriteria();
        criteria.andWxAppIdEqualTo(wxMarketingConfig.getWxAppId());
        if (wxMarketingConfigMapper.countByExample(example) == 0) {
            return wxMarketingConfigMapper.insertSelective(wxMarketingConfig);
        } else {
            return wxMarketingConfigMapper.updateByExampleSelective(wxMarketingConfig, example);
        }
    }

    public WxMarketingConfig query(Integer wxAppId) {
        WxMarketingConfigExample example = new WxMarketingConfigExample();
        WxMarketingConfigExample.Criteria criteria = example.createCriteria();
        criteria.andWxAppIdEqualTo(wxAppId);
        List<WxMarketingConfig> ret = wxMarketingConfigMapper.selectByExample(example);
        if (ret.size() == 1) {
            return ret.get(0);
        } else {
            return null;
        }
    }
}
