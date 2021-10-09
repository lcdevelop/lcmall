package com.lcsays.lcmall.db.service;

import com.lcsays.lcmall.db.dao.EcAddressMapper;
import com.lcsays.lcmall.db.model.EcAddress;
import com.lcsays.lcmall.db.model.EcAddressExample;
import com.lcsays.lcmall.db.model.WxMaUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-9-27 16:10
 */
@Service
public class EcAddressService {

    @Resource
    private EcAddressMapper ecAddressMapper;


    public List<EcAddress> getAddressesByUser(WxMaUser wxMaUser) {
        EcAddressExample example = new EcAddressExample();
        EcAddressExample.Criteria criteria = example.createCriteria();
        criteria.andWxMaUserIdEqualTo(wxMaUser.getId());
        return ecAddressMapper.selectByExample(example);
    }

    public EcAddress getAddressById(Integer addressId) {
        EcAddressExample example = new EcAddressExample();
        EcAddressExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(addressId);
        List<EcAddress> ret = ecAddressMapper.selectByExample(example);
        if (ret.size() == 1) {
            return ret.get(0);
        } else {
            return null;
        }
    }

    public int addAddress(EcAddress addr) {
        return ecAddressMapper.insert(addr);
    }

    public int updateAddress(EcAddress addr) {
        EcAddressExample example = new EcAddressExample();
        EcAddressExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(addr.getId());
        return ecAddressMapper.updateByExampleSelective(addr, example);
    }

    public int delAddress(Integer id) {
        EcAddressExample example = new EcAddressExample();
        EcAddressExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        return ecAddressMapper.deleteByExample(example);
    }
}
