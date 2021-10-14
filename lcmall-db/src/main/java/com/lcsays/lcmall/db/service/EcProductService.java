package com.lcsays.lcmall.db.service;

import com.lcsays.lcmall.db.dao.EcProductMapper;
import com.lcsays.lcmall.db.model.EcCategory;
import com.lcsays.lcmall.db.model.EcProduct;
import com.lcsays.lcmall.db.model.EcProductExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: lichuang
 * @Date: 21-9-27 16:10
 */
@Service
public class EcProductService {

    @Resource
    EcProductMapper ecProductMapper;

    public List<EcProduct> queryByCategoryId(Integer categoryId) {
        EcProductExample example = new EcProductExample();
        EcProductExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        return ecProductMapper.selectByExample(example);
    }

    public List<EcProduct> queryBy(List<Integer> categoryIds, String name, Integer categoryId) {
        EcProductExample example = new EcProductExample();
        EcProductExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdIn(categoryIds);
        if (null != name) {
            criteria.andNameEqualTo(name);
        }
        if (null != categoryId) {
            criteria.andCategoryIdEqualTo(categoryId);
        }
        return ecProductMapper.selectByExample(example);
    }

    public int insert(EcProduct product) {
        return ecProductMapper.insert(product);
    }

    public int update(EcProduct product) {
        EcProductExample example = new EcProductExample();
        EcProductExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(product.getId());
        return ecProductMapper.updateByExampleSelective(product, example);
    }

    public int deleteById(Integer id) {
        EcProductExample example = new EcProductExample();
        EcProductExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        return ecProductMapper.deleteByExample(example);
    }
}
