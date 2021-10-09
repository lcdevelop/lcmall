package com.lcsays.gg.utils;

import java.util.Random;

/**
 * @Author: lichuang
 * @Date: 21-10-8 12:48
 */

public class RequestNo {

    private static Random random = new Random();

    public static String randomWithCurTime(String prefix) {
        return prefix
                + "_"
                + TimeUtils.currentTimeStamp()
                + "_"
                + random.nextInt(10)
                + random.nextInt(10)
                + random.nextInt(10);
    }
}
