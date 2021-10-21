package com.lcsays.lcmall.db.util;

import com.lcsays.lcmall.db.model.WxMarketingActivity;

/**
 * @Author: lichuang
 * @Date: 21-10-21 20:27
 */

public class WxMarketingActivityUtil {

    public static void clearSecret(WxMarketingActivity wxMarketingActivity) {
        wxMarketingActivity.setStockIdList(null);
        wxMarketingActivity.setWxAppId(null);
    }
}
