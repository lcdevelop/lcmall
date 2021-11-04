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
    Integer pv; // stockIndexPageView
    Integer uv; // stockIndexPageView
    Integer clickPv; // getCouponInternal
    Integer clickUv; // getCouponInternal
    Integer consumeCount;
}
