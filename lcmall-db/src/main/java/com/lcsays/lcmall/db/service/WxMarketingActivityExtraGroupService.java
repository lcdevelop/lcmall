package com.lcsays.lcmall.db.service;

import com.lcsays.lcmall.db.dao.WxMarketingActivityExtraGroupMapper;
import com.lcsays.lcmall.db.model.WxMarketingActivityExtraGroup;
import com.lcsays.lcmall.db.model.WxMarketingActivityExtraGroupExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-9-27 16:10
 */
@Service
public class WxMarketingActivityExtraGroupService {

    @Resource
    private WxMarketingActivityExtraGroupMapper groupMapper;

    public List<WxMarketingActivityExtraGroup> queryByActivityId(Integer activityId) {
        WxMarketingActivityExtraGroupExample example = new WxMarketingActivityExtraGroupExample();
        WxMarketingActivityExtraGroupExample.Criteria criteria = example.createCriteria();
        criteria.andActivityIdEqualTo(activityId);
        return groupMapper.selectByExample(example);
    }
}
