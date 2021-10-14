package com.lcsays.lcmall.db.service;

import com.lcsays.lcmall.db.dao.EcSkuMapper;
import com.lcsays.lcmall.db.model.EcProductExample;
import com.lcsays.lcmall.db.model.EcSku;
import com.lcsays.lcmall.db.model.EcSkuExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-9-27 16:10
 */
@Service
public class EcSkuService {

    @Resource
    EcSkuMapper ecSkuMapper;

    public List<EcSku> queryBy(List<Integer> productIds, String name) {
        EcSkuExample example = new EcSkuExample();
        EcSkuExample.Criteria criteria = example.createCriteria();
        criteria.andProductIdIn(productIds);
        if (null != name) {
            criteria.andNameEqualTo(name);
        }
        return ecSkuMapper.selectByExample(example);
    }

    public int insert(EcSku ecSku) {
        return ecSkuMapper.insert(ecSku);
    }

    public EcSku queryById(Integer id) {
        EcSkuExample example = new EcSkuExample();
        EcSkuExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        List<EcSku> ret = ecSkuMapper.selectByExample(example);
        if (ret.size() == 1) {
            return ret.get(0);
        } else {
            return null;
        }
    }

    public int update(EcSku ecSku) {
        EcSkuExample example = new EcSkuExample();
        EcSkuExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(ecSku.getId());
        return ecSkuMapper.updateByExampleSelective(ecSku, example);
    }

    public int deleteById(Integer id) {
        EcSkuExample example = new EcSkuExample();
        EcSkuExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        return ecSkuMapper.deleteByExample(example);
    }
}
