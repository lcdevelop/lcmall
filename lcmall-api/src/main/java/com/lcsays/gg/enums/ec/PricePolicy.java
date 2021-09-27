package com.lcsays.gg.enums.ec;

/**
 * @Author: lichuang
 * @Date: 21-8-11 19:02
 */

public enum PricePolicy {
    PP_RAW(1);

    private int value;

    PricePolicy(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
