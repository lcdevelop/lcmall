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
        colors.add("#ED5565");
        colors.add("#FC6E51");
        colors.add("#FFCE54");
        colors.add("#A0D468");
        colors.add("#48CFAD");
        colors.add("#4FC1E9");
        colors.add("#5D9CEC");
        colors.add("#AC92EC");
        colors.add("#EC87C0");
    }

    public static String randomColor() {
        return colors.get(random.nextInt(colors.size()));
    }
}
