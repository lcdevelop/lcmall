package com.lcsays.lcmall.db.service;

import com.lcsays.lcmall.db.dao.EcCategoryMapper;
import com.lcsays.lcmall.db.model.EcCategory;
import com.lcsays.lcmall.db.model.EcCategoryExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-9-27 16:10
 */
@Service
public class EcCategoryService {

    @Resource
    EcCategoryMapper ecCategoryMapper;

    public List<EcCategory> queryByWxAppId(Integer wxAppId) {
        EcCategoryExample example = new EcCategoryExample();
        EcCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andWxAppIdEqualTo(wxAppId);
        return ecCategoryMapper.selectByExample(example);
    }

    public int insert(EcCategory c) {
        return ecCategoryMapper.insert(c);
    }

    public EcCategory queryById(Integer id) {
        if (null == id) {
            return null;
        }
        EcCategoryExample example = new EcCategoryExample();
        EcCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        List<EcCategory> ret = ecCategoryMapper.selectByExample(example);
        if (ret.size() == 1) {
            return ret.get(0);
        } else {
            return null;
        }
    }

    public int update(EcCategory c) {
        EcCategoryExample example = new EcCategoryExample();
        EcCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(c.getId());
        return ecCategoryMapper.updateByExampleSelective(c, example);
    }

    public void deleteById(Integer id) {
        EcCategoryExample example = new EcCategoryExample();
        EcCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        ecCategoryMapper.deleteByExample(example);
    }
}
