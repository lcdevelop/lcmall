package com.lcsays.gg.dao.weixin;

import com.lcsays.gg.models.ec.Address;
import com.lcsays.gg.models.manager.WxApp;
import com.lcsays.gg.models.weixin.WxMaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-8-6 12:31
 */
@Mapper
public interface AddressDao {
    List<Address> getAddressesByUser(@Param("wxMaUser") WxMaUser wxMaUser);
    Address getAddressById(Long addressId);
    int addAddress(@Param("address") Address address);
    int delAddress(Long addressId);
    int updateAddress(@Param("address") Address address);
}
