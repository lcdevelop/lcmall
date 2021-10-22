package com.lcsays.lcmall.db.service;

import com.lcsays.lcmall.db.dao.WxMarketingActivityExtraMapper;
import com.lcsays.lcmall.db.model.WxMarketingActivityExtra;
import com.lcsays.lcmall.db.model.WxMarketingActivityExtraExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-9-27 16:10
 */
@Service
public class WxMarketingActivityExtraService {

    @Resource
    private WxMarketingActivityExtraMapper extraMapper;

    public List<WxMarketingActivityExtra> queryByGroupIds(List<Integer> groupIds) {
        WxMarketingActivityExtraExample example = new WxMarketingActivityExtraExample();
        WxMarketingActivityExtraExample.Criteria criteria = example.createCriteria();
        criteria.andGroupIdIn(groupIds);
        return extraMapper.selectByExample(example);
    }
}
