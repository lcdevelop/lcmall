package com.lcsays.lcmall.db.service;

import com.lcsays.lcmall.db.dao.EcAddressMapper;
import com.lcsays.lcmall.db.dao.WxMaUserMapper;
import com.lcsays.lcmall.db.model.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-10-9 11:12
 */
@Service
public class WxMaUserService {

    @Resource
    private WxMaUserMapper wxMaUserMapper;

    @Resource
    private EcAddressMapper ecAddressMapper;

    public int updatePhoneNumber(Integer userId, String phoneNumber) {
        WxMaUserExample example = new WxMaUserExample();
        WxMaUserExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(userId);

        WxMaUser wxMaUser = new WxMaUser();
        wxMaUser.setPhoneNumber(phoneNumber);
        return wxMaUserMapper.updateByExampleSelective(wxMaUser, example);
    }


    public WxMaUser getWxMaUserById(Integer id) {
        WxMaUserExample example = new WxMaUserExample();
        WxMaUserExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        List<WxMaUser> ret = wxMaUserMapper.selectByExample(example);
        if (ret.size() == 1) {
            return ret.get(0);
        } else {
            return null;
        }
    }

    public WxMaUser getWxMaUserByOpenid(WxApp wxApp, String openId) {
        WxMaUserExample example = new WxMaUserExample();
        WxMaUserExample.Criteria criteria = example.createCriteria();
        criteria.andWxAppIdEqualTo(wxApp.getId());
        criteria.andOpenidEqualTo(openId);
        List<WxMaUser> ret = wxMaUserMapper.selectByExample(example);
        if (ret.size() == 1) {
            return ret.get(0);
        } else {
            return null;
        }
    }

    public int update(WxMaUser wxMaUser) {
        WxMaUserExample example = new WxMaUserExample();
        WxMaUserExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(wxMaUser.getId());
        return wxMaUserMapper.updateByExampleSelective(wxMaUser, example);
    }

    public long insert(WxMaUser wxMaUser) {
        return wxMaUserMapper.insert(wxMaUser);
    }
}