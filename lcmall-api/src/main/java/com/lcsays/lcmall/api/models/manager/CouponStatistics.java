package com.lcsays.lcmall.api.models.manager;

import com.lcsays.lcmall.api.utils.TimeUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-10-20 9:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponStatistics {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CouponItem {
        private String stockId;
        private String couponId;
        private String status;
        private Integer transactionMinimum; // 门槛
        private Integer couponAmount; // 面额
        private Date consumeTime;
        private String consumeMchid;
        private String transactionId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CouponsInfo {
        private List<CouponItem> coupons;
        private Integer consumeCount;
        private Integer consumeAmount;
    }

    private String whitelistPhoneNumber;
    private boolean wxMaUserHasPhoneNumber; // 对应的用户表里是否授权了手机号
    private CouponsInfo couponsInfo;
}
