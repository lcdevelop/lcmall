package com.lcsays.gg.service.weixin;

import com.lcsays.gg.models.ec.Address;
import com.lcsays.gg.models.manager.WxApp;
import com.lcsays.gg.models.weixin.WxMaUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-9-13 16:20
 */

public interface UserService {
    long insert(WxMaUser wxMaUser);
    void update(WxMaUser wxMaUser);
    WxMaUser getWxMaUserByOpenid(@Param("wxApp") WxApp wxApp, @Param("openId") String openId);
    WxMaUser getWxMaUserById(@Param("wxApp")WxApp wxApp, @Param("id") Long id);
    WxMaUser getWxMaUserBySimpleId(@Param("id") Long id);
    WxMaUser getWxMaUserBySessionKey(@Param("sessionKey") String sessionKey);
    List<WxMaUser> userList(String nickName);

    WxMaUser getWxMaUserById(String appId, Long id);

    List<Address> getAddressesByUser(WxMaUser wxMaUser);
    Address getAddressById(Long addressId);
    int addAddress(Address address);
    int delAddress(Long addressId);
    int updateAddress(Address address);
}
