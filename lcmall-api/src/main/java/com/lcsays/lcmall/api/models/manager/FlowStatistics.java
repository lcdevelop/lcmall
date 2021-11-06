package com.lcsays.lcmall.api.models.manager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: lichuang
 * @Date: 21-10-26 23:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlowStatistics {
    // 累计总数
    Integer totalPv; // stockIndexPageView
    Integer totalUv; // stockIndexPageView
    Integer totalClickPv; // getCouponInternal
    Integer totalClickUv; // getCouponInternal
    Integer totalCouponCount; // 领券数
    Integer totalConsumeCount; // 核销数

    // 指定日期
    Integer pv; // stockIndexPageView
    Integer uv; // stockIndexPageView
    Integer clickPv; // getCouponInternal
    Integer clickUv; // getCouponInternal
    Integer couponCount; // 领券数
    Integer consumeCount; // 核销数
}
