package com.lcsays.lcmall.db.service;

import com.lcsays.lcmall.db.dao.WxMarketingWhitelistMapper;
import com.lcsays.lcmall.db.model.WxMarketingWhitelist;
import com.lcsays.lcmall.db.model.WxMarketingWhitelistExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Author: lichuang
 * @Date: 21-9-27 16:10
 */
@Service
public class WxMarketingWhitelistService {

    @Resource
    private WxMarketingWhitelistMapper wxMarketingWhitelistMapper;

    public boolean contains(Integer batchNo, String phoneNumber) {
        if (null != phoneNumber) {
            WxMarketingWhitelistExample example = new WxMarketingWhitelistExample();
            WxMarketingWhitelistExample.Criteria criteria = example.createCriteria();
            criteria.andBatchNoEqualTo(batchNo);
            criteria.andPhoneNumberEqualTo(phoneNumber);
            return wxMarketingWhitelistMapper.countByExample(example) > 0;
        } else {
            return false;
        }
    }

    public List<WxMarketingWhitelist> queryByBatchNo(Integer batchNo) {
        WxMarketingWhitelistExample example = new WxMarketingWhitelistExample();
        WxMarketingWhitelistExample.Criteria criteria = example.createCriteria();
        criteria.andBatchNoEqualTo(batchNo);
        return wxMarketingWhitelistMapper.selectByExample(example);
    }

    public List<WxMarketingWhitelist> queryByBatchNoAndPhoneNumbers(Integer batchNo, List<String> phoneNumbers) {
        WxMarketingWhitelistExample example = new WxMarketingWhitelistExample();
        WxMarketingWhitelistExample.Criteria criteria = example.createCriteria();
        criteria.andBatchNoEqualTo(batchNo);
        criteria.andPhoneNumberIn(phoneNumbers);
        return wxMarketingWhitelistMapper.selectByExample(example);
    }

    public List<WxMarketingWhitelist> queryByBatchNoAndPhoneNumber(Integer batchNo, String phoneNumber) {
        WxMarketingWhitelistExample example = new WxMarketingWhitelistExample();
        WxMarketingWhitelistExample.Criteria criteria = example.createCriteria();
        criteria.andBatchNoEqualTo(batchNo);
        criteria.andPhoneNumberEqualTo(phoneNumber);
        return wxMarketingWhitelistMapper.selectByExample(example);
    }

}
