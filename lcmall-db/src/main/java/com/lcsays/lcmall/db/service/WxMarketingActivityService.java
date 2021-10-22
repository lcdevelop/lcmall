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
    private WxMarketingActivityMapper activityMapper;

    public List<WxMarketingActivity> queryByWxAppId(Integer wxAppId) {
        WxMarketingActivityExample example = new WxMarketingActivityExample();
        WxMarketingActivityExample.Criteria criteria = example.createCriteria();
        criteria.andWxAppIdEqualTo(wxAppId);
        return activityMapper.selectByExample(example);
    }

    public WxMarketingActivity queryById(Integer id) {
        WxMarketingActivityExample example = new WxMarketingActivityExample();
        WxMarketingActivityExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        List<WxMarketingActivity> ret = activityMapper.selectByExample(example);
        if (ret.size() == 1) {
            return ret.get(0);
        } else {
            return null;
        }
    }

    public int update(WxMarketingActivity activity) {
        WxMarketingActivityExample example = new WxMarketingActivityExample();
        WxMarketingActivityExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(activity.getId());
        return activityMapper.updateByExampleSelective(activity, example);
    }

}
