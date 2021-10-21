package com.lcsays.lcmall.api.service.weixin.impl;

import com.lcsays.lcmall.api.dao.manager.WxAppDao;
import com.lcsays.lcmall.api.dao.weixin.AddressDao;
import com.lcsays.lcmall.api.dao.weixin.WxMaUserDao;
import com.lcsays.lcmall.api.models.ec.Address;
import com.lcsays.lcmall.api.models.manager.WxApp;
import com.lcsays.lcmall.api.models.weixin.WxMaUser;
import com.lcsays.lcmall.api.service.weixin.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-9-13 16:21
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    WxMaUserDao wxMaUserDao;

    @Resource
    WxAppDao wxAppDao;

    @Resource
    AddressDao addressDao;

    @Override
    public long insert(WxMaUser wxMaUser) {
        return wxMaUserDao.insert(wxMaUser);
    }

    @Override
    public void update(WxMaUser wxMaUser) {
        wxMaUserDao.update(wxMaUser);
    }

    @Override
    public WxMaUser getWxMaUserByOpenid(WxApp wxApp, String openId) {
        return wxMaUserDao.getWxMaUserByOpenid(wxApp, openId);
    }

    @Override
    public WxMaUser getWxMaUserById(WxApp wxApp, Long id) {
        return wxMaUserDao.getWxMaUserById(wxApp, id);
    }

    @Override
    public WxMaUser getWxMaUserBySimpleId(Long id) {
        return wxMaUserDao.getWxMaUserBySimpleId(id);
    }

    @Override
    public WxMaUser getWxMaUserBySessionKey(String sessionKey) {
        return wxMaUserDao.getWxMaUserBySessionKey(sessionKey);
    }

    @Override
    public List<WxMaUser> userList(String nickName) {
        return wxMaUserDao.userList(nickName);
    }

    @Override
    public WxMaUser getWxMaUserById(String appId, Long id) {
        WxApp wxApp = wxAppDao.getWxAppByAppId(appId);
        if (null == wxApp) {
            return null;
        } else {
            return wxMaUserDao.getWxMaUserById(wxApp, id);
        }
    }

    @Override
    public List<Address> getAddressesByUser(WxMaUser wxMaUser) {
        return addressDao.getAddressesByUser(wxMaUser);
    }

    @Override
    public Address getAddressById(Long addressId) {
        return addressDao.getAddressById(addressId);
    }

    @Override
    public int addAddress(Address address) {
        return addressDao.addAddress(address);
    }

    @Override
    public int delAddress(Long addressId) {
        return addressDao.delAddress(addressId);
    }

    @Override
    public int updateAddress(Address address) {
        return addressDao.updateAddress(address);
    }
}
