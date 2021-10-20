package com.lcsays.lcmall.db.service;

import com.lcsays.lcmall.db.dao.WxMarketingStockMapper;
import com.lcsays.lcmall.db.model.WxMarketingStock;
import com.lcsays.lcmall.db.model.WxMarketingStockExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: lichuang
 * @Date: 21-9-27 16:10
 */
@Service
public class WxMarketingStockService {

    @Resource
    private WxMarketingStockMapper marketingStockMapper;

    public int createOrUpdate(WxMarketingStock wxMarketingStock) {
        WxMarketingStockExample example = new WxMarketingStockExample();
        WxMarketingStockExample.Criteria criteria = example.createCriteria();
        criteria.andWxAppIdEqualTo(wxMarketingStock.getWxAppId());
        criteria.andStockIdEqualTo(wxMarketingStock.getStockId());
        List<WxMarketingStock> stocks = marketingStockMapper.selectByExample(example);
        if (stocks.size() == 0) {
            // 没有，新插入
            return marketingStockMapper.insert(wxMarketingStock);
        } else {
            // 有了，做更新
            criteria.andIdEqualTo(stocks.get(0).getId());
            wxMarketingStock.setId(stocks.get(0).getId());
            return marketingStockMapper.updateByExample(wxMarketingStock, example);
        }
    }

    public WxMarketingStock queryByStockId(String stockId) {
        WxMarketingStockExample example = new WxMarketingStockExample();
        WxMarketingStockExample.Criteria criteria = example.createCriteria();
        criteria.andStockIdEqualTo(stockId);
        List<WxMarketingStock> stocks = marketingStockMapper.selectByExample(example);
        if (stocks.size() == 1) {
            return stocks.get(0);
        } else {
            return null;
        }
    }

    public Map<String, WxMarketingStock> getStocksMap() {
        Map<String, WxMarketingStock> ret = new HashMap<>();
        WxMarketingStockExample example = new WxMarketingStockExample();
        WxMarketingStockExample.Criteria criteria = example.createCriteria();
        criteria.andIdIsNotNull();
        for (WxMarketingStock stock: marketingStockMapper.selectByExample(example)) {
            ret.put(stock.getStockId(), stock);
        }
        return ret;
    }
}
