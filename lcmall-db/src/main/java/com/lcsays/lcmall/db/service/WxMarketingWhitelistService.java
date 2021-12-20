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

    public boolean contains(Integer batchNo, String userPhoneEncrypt) {
        if (null != userPhoneEncrypt) {
            WxMarketingWhitelistExample example = new WxMarketingWhitelistExample();
            WxMarketingWhitelistExample.Criteria criteria = example.createCriteria();
            criteria.andBatchNoEqualTo(batchNo);
            criteria.andUserPhoneEncryptEqualTo(userPhoneEncrypt);
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

    public List<WxMarketingWhitelist> queryByBatchNoAndUserPhoneEncrypts(Integer batchNo, List<String> userPhoneEncrypts) {
        WxMarketingWhitelistExample example = new WxMarketingWhitelistExample();
        WxMarketingWhitelistExample.Criteria criteria = example.createCriteria();
        criteria.andBatchNoEqualTo(batchNo);
        criteria.andUserPhoneEncryptIn(userPhoneEncrypts);
        return wxMarketingWhitelistMapper.selectByExample(example);
    }

    public List<WxMarketingWhitelist> queryByBatchNoAndUserPhoneEncrypt(Integer batchNo, String userPhoneEncrypt) {
        WxMarketingWhitelistExample example = new WxMarketingWhitelistExample();
        WxMarketingWhitelistExample.Criteria criteria = example.createCriteria();
        criteria.andBatchNoEqualTo(batchNo);
        criteria.andUserPhoneEncryptEqualTo(userPhoneEncrypt);
        return wxMarketingWhitelistMapper.selectByExample(example);
    }

}
