package com.lcsays.lcmall.db.util;

import com.lcsays.lcmall.db.model.WxMarketingActivity;

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
}
