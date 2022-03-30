package com.lcsays.lcmall.api.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ColorUtils {
    private static List<String> colors = new ArrayList<>();
    private static Random random = new Random();

    private static String int2Color(int num) {
        String s = Integer.toHexString(num);
        return StringUtils.leftPad(s, 6, '0');
    }

    static {
        for (int i = 0; i < 16777215; i += 559240) {
            colors.add("#" + int2Color(i));
        }
    }

    public static String randomColor() {
        return colors.get(random.nextInt(colors.size()));
    }
}
