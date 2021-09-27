package com.lcsays.gg.utils;

import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-9-2 18:27
 */

public class ApiUtils {
    public static  <T> List<T> pagination(List<T> data, int current, int pageSize) {
        int fromIndex = (current-1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, data.size());
        return data.subList(fromIndex, toIndex);
    }
}
