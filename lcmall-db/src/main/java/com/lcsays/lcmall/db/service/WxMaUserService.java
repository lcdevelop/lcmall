package com.lcsays.lcmall.db.service;

import com.lcsays.lcmall.db.dao.WxMaUserMapper;
import com.lcsays.lcmall.db.model.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-10-9 11:12
 */
@Service
public class WxMaUserService {

    @Resource
    private WxMaUserMapper wxMaUserMapper;

    public int updatePhoneNumber(Integer userId, String userPhone, String userPhoneEncrypt) {
        WxMaUserExample example = new WxMaUserExample();
        WxMaUserExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(userId);

        WxMaUser wxMaUser = new WxMaUser();
        wxMaUser.setUserPhone(userPhone);
        wxMaUser.setUserPhoneEncrypt(userPhoneEncrypt);
        wxMaUser.setUpdateTime(new Date());
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
        wxMaUser.setUpdateTime(new Date());
        return wxMaUserMapper.updateByExampleSelective(wxMaUser, example);
    }

    public long insert(WxMaUser wxMaUser) {
        wxMaUser.setCreateTime(new Date());
        wxMaUser.setUpdateTime(new Date());
        return wxMaUserMapper.insert(wxMaUser);
    }

    public WxMaUser getWxMaUserBySessionKey(String shortSid) {
        WxMaUserExample example = new WxMaUserExample();
        WxMaUserExample.Criteria criteria = example.createCriteria();
        criteria.andSessionKeyEqualTo(shortSid);
        List<WxMaUser> ret = wxMaUserMapper.selectByExample(example);
        if (ret.size() == 1) {
            return ret.get(0);
        } else {
            return null;
        }
    }

    public List<WxMaUser> queryUsersByNickName(String nickName) {
        WxMaUserExample example = new WxMaUserExample();
        WxMaUserExample.Criteria criteria = example.createCriteria();
        criteria.andNicknameEqualTo(nickName);
        return wxMaUserMapper.selectByExample(example);
    }

    public WxMaUser queryUsersByToken(String token) {
        if (null == token) {
            return null;
        }
        WxMaUserExample example = new WxMaUserExample();
        WxMaUserExample.Criteria criteria = example.createCriteria();
        criteria.andTokenEqualTo(token);
        List<WxMaUser> ret = wxMaUserMapper.selectByExample(example);
        if (ret.size() == 1) {
            return ret.get(0);
        } else {
            return null;
        }
    }

    public List<WxMaUser> listUsers() {
        WxMaUserExample example = new WxMaUserExample();
        WxMaUserExample.Criteria criteria = example.createCriteria();
        criteria.andIdIsNotNull();
        return wxMaUserMapper.selectByExample(example);
    }

    public List<WxMaUser> listUsersWithPhoneNumberByWxAppId(Integer wxAppId) {
        WxMaUserExample example = new WxMaUserExample();
        WxMaUserExample.Criteria criteria = example.createCriteria();
        criteria.andWxAppIdEqualTo(wxAppId);
        criteria.andUserPhoneIsNotNull();
        return wxMaUserMapper.selectByExample(example);
    }

    public WxMaUser queryUserByWxAppIdAndUserPhoneEncrypt(Integer wxAppId, String userPhoneEncrypt) {
        WxMaUserExample example = new WxMaUserExample();
        WxMaUserExample.Criteria criteria = example.createCriteria();
        criteria.andWxAppIdEqualTo(wxAppId);
        criteria.andUserPhoneEncryptEqualTo(userPhoneEncrypt);
        List<WxMaUser> ret = wxMaUserMapper.selectByExample(example);
        if (ret.size() == 1) {
            return ret.get(0);
        } else {
            return null;
        }
    }

    public WxMaUser queryUserByEmail(String email) {
        WxMaUserExample example = new WxMaUserExample();
        example.createCriteria().andEmailEqualTo(email);
        List<WxMaUser> users = wxMaUserMapper.selectByExample(example);
        if (null != users && users.size() > 0) {
            return users.get(0);
        } else {
            return null;
        }
    }

    public WxMaUser createUserWithEmail(String email) {
        WxMaUser user = new WxMaUser();
        user.setEmail(email);
        if (wxMaUserMapper.insertSelective(user) > 0) {
            return user;
        } else {
            return null;
        }
    }
}
