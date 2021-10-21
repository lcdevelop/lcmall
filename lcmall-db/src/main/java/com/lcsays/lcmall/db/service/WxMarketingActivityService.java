package com.lcsays.lcmall.db.service;

import com.lcsays.lcmall.db.dao.WxMarketingActivityMapper;
import com.lcsays.lcmall.db.model.WxMarketingActivity;
import com.lcsays.lcmall.db.model.WxMarketingActivityExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-9-27 16:10
 */
@Service
public class WxMarketingActivityService {

    @Resource
    private WxMarketingActivityMapper wxMarketingActivityMapper;

    public List<WxMarketingActivity> queryByWxAppId(Integer wxAppId) {
        WxMarketingActivityExample example = new WxMarketingActivityExample();
        WxMarketingActivityExample.Criteria criteria = example.createCriteria();
        criteria.andWxAppIdEqualTo(wxAppId);
        return wxMarketingActivityMapper.selectByExample(example);
    }

    public int update(WxMarketingActivity activity) {
        WxMarketingActivityExample example = new WxMarketingActivityExample();
        WxMarketingActivityExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(activity.getId());
        return wxMarketingActivityMapper.updateByExampleSelective(activity, example);
    }
}
