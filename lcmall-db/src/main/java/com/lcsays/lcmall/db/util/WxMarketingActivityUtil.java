package com.lcsays.lcmall.db.util;

import com.lcsays.lcmall.db.model.WxMarketingActivity;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author: lichuang
 * @Date: 21-10-21 20:27
 */

public class WxMarketingActivityUtil {

    public static String[] getStockIds(WxMarketingActivity activity) {
        if (null != activity && null != activity.getStockIdList()) {
            return activity.getStockIdList().split(",");
        } else {
            return new String[]{};
        }
    }

    public static Set<String> getStockIdsSet(WxMarketingActivity activity) {
        Set<String> ret = new HashSet<>();
        for (String stockId: getStockIds(activity)) {
            ret.add(stockId);
        }
        return ret;
    }
}
