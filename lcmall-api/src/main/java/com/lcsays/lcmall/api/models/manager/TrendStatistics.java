package com.lcsays.lcmall.api.models.manager;

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
public class TrendStatistics {
    private List<String> keys;
    private List<Integer> values;
}
