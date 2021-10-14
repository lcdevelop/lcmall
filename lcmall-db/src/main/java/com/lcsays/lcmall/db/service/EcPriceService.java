package com.lcsays.lcmall.db.service;

import com.lcsays.lcmall.db.dao.EcPriceMapper;
import com.lcsays.lcmall.db.model.EcPrice;
import com.lcsays.lcmall.db.model.EcPriceExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-9-27 16:10
 */
@Service
public class EcPriceService {

    @Resource
    EcPriceMapper ecPriceMapper;

    public EcPrice queryBySkuId(Integer skuId) {
        EcPriceExample example = new EcPriceExample();
        EcPriceExample.Criteria criteria = example.createCriteria();
        criteria.andSkuIdEqualTo(skuId);
        List<EcPrice> ret = ecPriceMapper.selectByExample(example);
        if (ret.size() == 1) {
            return ret.get(0);
        } else {
            return null;
        }
    }

    public int insert(EcPrice ecPrice) {
        return ecPriceMapper.insert(ecPrice);
    }

    public int update(EcPrice ecPrice) {
        EcPriceExample example = new EcPriceExample();
        EcPriceExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(ecPrice.getId());
        return ecPriceMapper.updateByExampleSelective(ecPrice, example);
    }
}
